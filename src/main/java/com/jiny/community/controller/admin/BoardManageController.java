package com.jiny.community.controller.admin;

import com.jiny.community.dto.CategoryDto;
import com.jiny.community.dto.Post.CategoryResponseDto;
import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
        if(categoryDto.getId()==1 || categoryDto.getId()==2){
            return "admin";
        }
        categoryService.removeCategory(categoryDto.getId());
        return "admin";
    }
    @PostMapping(value = "/category")
    @ResponseBody
    public String addCategory(String categoryName){
        log.info("categoryName ={}",categoryName);
        categoryService.addCategory(categoryName);

        return "admin";
    }


}
