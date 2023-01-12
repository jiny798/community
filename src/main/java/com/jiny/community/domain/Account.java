package com.jiny.community.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name="account_id")
    private Long id;

    @Column(nullable = false) // unique = true 테스트를 위해 임시로 제거
    private String email;

    private String nickname;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<UserLikePost> userLikePosts = new ArrayList<>();

}
