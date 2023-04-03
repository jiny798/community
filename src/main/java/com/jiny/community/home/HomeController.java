package com.jiny.community.home;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.service.CategoryService;
import com.jiny.community.board.dto.PostResponseDto;
import com.jiny.community.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequiredArgsConstructor @Slf4j
public class HomeController {

    private final CategoryService categoryService;
    private final AccountRepository accountRepository;
    private final PostService postService;
    @GetMapping(value = "/")
    public String home(HttpServletRequest request, @CurrentUser Account account, Model model) throws UnsupportedEncodingException {
//        String str = URLEncoder.encode("자유게시판", "UTF-8");
//        return "redirect:/post/list/"+str;
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
        }
        Page<PostResponseDto> postPage =postService.getPagingList(0,"공지사항","id");
        log.debug("postPage=={}",postPage);
        model.addAttribute("postList",postPage);



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
