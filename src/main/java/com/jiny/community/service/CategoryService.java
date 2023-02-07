package com.jiny.community.service;

import com.jiny.community.domain.Category;
import com.jiny.community.dto.Post.CategoryResponseDto;
import com.jiny.community.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public List<CategoryResponseDto> getCategoryNames(){
        List<Category> catelist = categoryRepository.findAll();
        List<CategoryResponseDto> list = catelist.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))
                .collect(Collectors.toList());


        return list;
    }

    public void removeCategory(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        categoryRepository.delete(category.get());
    }
}
