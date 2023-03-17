package com.jiny.community.admin.service;

import com.jiny.community.admin.domain.Category;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional @Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(String categoryName){
        Category category = new Category();
        category.setName(categoryName);
        categoryRepository.save(category);
    }


//    public List<Category> findAll(){
//
//        log.debug("findAll 수행");
//        return categoryRepository.findAll();
//    }

    @Cacheable(value = "categorys",key="", unless = "#result == null") //캐시 key = categorys 로 들어감
    public List<CategoryResponseDto> getCategoryNames(){
        List<Category> catelist = categoryRepository.findAll();
        List<CategoryResponseDto> list = catelist.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))// 프록시라서 한버 더 호출 ?
                .collect(Collectors.toList());


        return list;
    }

    public void removeCategory(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        categoryRepository.delete(category.get());
    }
}
