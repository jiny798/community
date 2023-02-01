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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller @Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardManageController {
    private final CategoryService categoryService;

    @GetMapping(value = "/")
    public String getAdminPage(Model model){
        List<String> categorylist = categoryService.getCategoryNames();

        log.info("카테고리 사이즈 = {}",categorylist.size());
        model.addAttribute("category_list",categorylist);

        return "admin/board";
    }

    @PostMapping(value = "/category")
    public String addCategory(String categoryName){
        log.info("categoryName ={}",categoryName);
        categoryService.addCategory(categoryName);

        return "redirect:/admin/";
    }
//    @PostMapping(value = "/category")
//    public RedirectView addCategory(String categoryName){
//        log.info("categoryName ={}",categoryName);
//        categoryService.addCategory(categoryName);
//
//        return new RedirectView("/admin/");
//    }


}
