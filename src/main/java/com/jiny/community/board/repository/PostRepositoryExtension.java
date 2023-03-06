package com.jiny.community.board.repository;

import com.jiny.community.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepositoryExtension {
    Page<Post> findByKeyword(String keyword, Pageable pageable);
}
