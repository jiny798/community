package com.jiny.community.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.dto.Post.CategoryResponseDto;
import com.jiny.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final AccountRepository accountRepository;
    @GetMapping(value = "/")
    public String home(HttpServletRequest request, @CurrentUser Account account, Model model) throws UnsupportedEncodingException {
//        String str = URLEncoder.encode("자유게시판", "UTF-8");
//        return "redirect:/post/list/"+str;
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
        }
        return "home";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(){
        return "account/login";
    }

    @GetMapping(value = "/categorys")
    public String getCategorys(Model model){
        List<CategoryResponseDto> categorylist = categoryService.getCategoryNames();
        model.addAttribute("category_list",categorylist);

       return "fragment/left_menu :: #category_ul";
    }
}
