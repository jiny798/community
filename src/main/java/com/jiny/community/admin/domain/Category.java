package com.jiny.community.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiny.community.board.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id") @JsonIgnore
    private Post post;

}
