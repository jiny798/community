package com.jiny.community.repository;

import com.jiny.community.domain.Category;
import com.jiny.community.domain.Comment;
import com.jiny.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findByTitle(String title);

    @Query("select p from Post p where p.category = :category")
    public List<Post> findByCategory(@Param("category") Category category);

    Page<Post> findByCategory(Category category , Pageable pageable);
}
