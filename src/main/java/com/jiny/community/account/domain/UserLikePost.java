package com.jiny.community.account.domain;

import com.jiny.community.board.domain.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class UserLikePost {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static UserLikePost createLikePost( Post post ){
        UserLikePost userLikePost = new UserLikePost();
        userLikePost.setPost(post);

        return userLikePost;
    }
}
