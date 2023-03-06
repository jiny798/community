package com.jiny.community.board.repository;

import com.jiny.community.admin.domain.Category;
import com.jiny.community.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>,PostRepositoryExtension {

    public List<Post> findByTitle(String title);

    @Query("select p from Post p where p.category = :category")
    public List<Post> findByCategory(@Param("category") Category category);

    Page<Post> findByCategory(Category category , Pageable pageable);
}
