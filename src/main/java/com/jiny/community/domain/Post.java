package com.jiny.community.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private LocalDateTime CreatedDate;

    private String title;
    private String content;
    private Long viewCnt;
    private Long star;
    private Boolean remove;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id") //FK 이름
    private Account account;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<UserLikePost> userLikePosts = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public void setUser(Account account){
        this.account = account;
        account.getPostList().add(this);
    }

    public void addStar(){
        this.star++;
    }

    public void decreaseStar(){
        this.star--;
    }

    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setPost(this);
    }

    public static Post createPost(Account account,String title ,String content){
        Post post = new Post();
        post.setUser(account);
        post.setCreatedDate(LocalDateTime.now());

        post.setTitle(title);
        post.setContent(content);
        post.setViewCnt(0L);
        post.setStar(0L);
        post.setRemove(false);
        return post;
    }

}
