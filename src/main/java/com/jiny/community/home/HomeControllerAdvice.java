package com.jiny.community.home;

import com.jiny.community.admin.service.CategoryService;
import com.jiny.community.board.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class HomeControllerAdvice {
    private final CategoryService categoryService;

    @ModelAttribute("category_list")//카테 고리 리스트 반환
    public List<CategoryResponseDto> categoryNames(){
        List<CategoryResponseDto> category_list = categoryService.getCategoryNames();

        return category_list;
    }
}
