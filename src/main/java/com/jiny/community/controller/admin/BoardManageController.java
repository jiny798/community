package com.jiny.community.controller.admin;

import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller @Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardManageController {
    private final CategoryService categoryService;

    @GetMapping(value = "")
    public String getAdminPage(Model model){
        List<String> categorylist = categoryService.getCategoryNames();

        model.addAttribute("category_list",categorylist);

        return "admin/board";
    }


    @PostMapping(value = "/category")
    public String addCategory(String categoryName){
        categoryService.addCategory(categoryName);
        return "admin/board";
    }



}
