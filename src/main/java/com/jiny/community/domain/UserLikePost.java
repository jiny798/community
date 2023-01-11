package com.jiny.community.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UserLikePost {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static UserLikePost createLikePost( Post post ){
        UserLikePost userLikePost = new UserLikePost();
        userLikePost.setPost(post);

        return userLikePost;
    }
}
