package com.jiny.community.admin.repository;

import com.jiny.community.admin.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
//List<Comment> findByPostId(Long postId);
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
