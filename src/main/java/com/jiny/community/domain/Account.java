package com.jiny.community.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
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

    private boolean isValid;

    private String emailToken;

    public void createRandomToken(){
        this.emailToken = UUID.randomUUID().toString();
    }

    private String password;
    private String role;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
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

}
