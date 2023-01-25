package com.jiny.community.service;

import com.jiny.community.domain.Category;
import com.jiny.community.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getCategoryNames(){
        List<Category> catelist = categoryRepository.findAll();
        List<String> list = catelist.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        return list;
    }
}
