package com.jiny.community.service;

import com.jiny.community.domain.Category;
import com.jiny.community.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(String categoryName){
        Category category = new Category();
        category.setName(categoryName);
        categoryRepository.save(category);
    }
}
