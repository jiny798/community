package com.jiny.community.repository;

import com.jiny.community.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
//List<Comment> findByPostId(Long postId);
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}