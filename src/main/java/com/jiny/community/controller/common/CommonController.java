package com.jiny.community.controller.common;

import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class CommonController {
    private final CategoryService categoryService;

    @ModelAttribute("category_list")
    public List<CategoryResponseDto> categoryNames(){
        List<CategoryResponseDto> category_list = categoryService.getCategoryNames();


        return category_list;
    }
}
