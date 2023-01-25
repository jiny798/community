package com.jiny.community.admin;

import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardManageController {
    private final CategoryService categoryService;

    @GetMapping(value = "")
    public String getAdminPage(){
        return "admin/board";
    }


    @PostMapping(value = "/category")
    public String addCategory(String categoryName){
        categoryService.addCategory(categoryName);
        return "admin/board";
    }

}
