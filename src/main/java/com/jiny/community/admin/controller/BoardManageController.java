package com.jiny.community.admin.controller;

import com.jiny.community.admin.domain.Category;
import com.jiny.community.admin.dto.CategoryDto;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.repository.CategoryRepository;
import com.jiny.community.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller @Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardManageController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping(value = "/")
    public String getAdminPage(Model model){
        List<CategoryResponseDto> categorylist = categoryService.getCategoryNames();

        log.info("카테고리 사이즈 = {}",categorylist.size());
        model.addAttribute("category_list",categorylist);

        return "admin/board";
    }

    @PostMapping(value = "/category/delete")
    @ResponseBody
    public String deleteCategory(CategoryDto categoryDto){
        log.info("categoryName ={}",categoryDto.getId());
        if(categoryDto.getName().equals("자유게시판") || categoryDto.getName().equals("공지사항") || categoryDto.getName().equals("Q&A")){
            return "false";
        }
        categoryService.removeCategory(categoryDto.getId());
        return "admin";
    }
    @PostMapping(value = "/category")
    @ResponseBody
    public String addCategory(String categoryName){
        log.info("categoryName ={}",categoryName);

        Category findCate=categoryRepository.findByName(categoryName);
        if(findCate == null){
            categoryService.addCategory(categoryName);
        }else {
            return "false";
        }
        return "admin";
    }


}
