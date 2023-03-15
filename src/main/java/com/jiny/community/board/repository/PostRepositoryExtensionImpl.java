package com.jiny.community.board.repository;

import com.jiny.community.board.domain.Post;
import com.jiny.community.board.domain.QPost;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    public PostRepositoryExtensionImpl(){
        super(Post.class);
    }

    @Override
    public Page<Post> findByKeyword(String keyword, Pageable pageable){
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post)
                .where(post.title.containsIgnoreCase(keyword)
                        .or(post.account.nickname.containsIgnoreCase(keyword))).orderBy(post.id.desc()
                );

        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable,query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(),pageable, fetchResults.getTotal());

    }
}
