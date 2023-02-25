package com.jiny.community.account.domain;

import com.jiny.community.account.domain.CommonAttribute.NotificationSetting;
import com.jiny.community.account.domain.CommonAttribute.Profile;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.domain.Post;
import com.jiny.community.settings.controller.NotificationForm;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter @Setter @Builder @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name="account_id")
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;
    private String role;
    private boolean isValid;

    private String emailToken;
    private LocalDateTime emailTokenGeneratedAt;

    @Embedded
    private Profile profile = new Profile();

    @Embedded
    private NotificationSetting notificationSetting = new NotificationSetting();

    public void createRandomToken(){
        this.emailToken = UUID.randomUUID().toString();
        this.emailTokenGeneratedAt = LocalDateTime.now();
    }
    public boolean enableSendEmail(){
        return this.emailTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL) @ToString.Exclude
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL) @ToString.Exclude
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL) @ToString.Exclude
    private List<UserLikePost> userLikePosts = new ArrayList<>();

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public static Account createAccount(String email, String nickname, String password) {
        Account account = new Account();
        account.email = email;
        account.nickname = nickname;
        account.password = password;
        return account;
    }

    private LocalDateTime joinedAt;
    public void verified() {
        this.isValid = true;
        joinedAt = LocalDateTime.now();
    }

    public void addLikePost(UserLikePost userLikePost){
        this.userLikePosts.add(userLikePost);
        userLikePost.setAccount(this);
    }

    @PostLoad
    private void init(){
        if(profile == null){
            profile = new Profile();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void updateProfile(com.jiny.community.settings.controller.Profile profile){
        if(this.profile ==null){
            this.profile = new Profile();
        }
        this.profile.company=profile.getCompany();
        this.profile.bio=profile.getBio();
        this.profile.image = profile.getImage();
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }
    public void updateNotification(NotificationForm notificationForm) {
        this.notificationSetting.CommentCreatedByEmail = notificationForm.isCommentCreatedByEmail();
        this.notificationSetting.CommentCreatedByWeb = notificationForm.isCommentCreatedByWeb();

    }

    public boolean isValid(String token){
        return this.emailToken.equals(token);
    }
}
