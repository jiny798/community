package com.jiny.community.controller.common;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class CommonController {
    private final CategoryService categoryService;
    private final AccountRepository accountRepository;

    @ModelAttribute("category_list")
    public List<CategoryResponseDto> categoryNames(){
        List<CategoryResponseDto> category_list = categoryService.getCategoryNames();

        return category_list;
    }

    public void commonAccount(Model model, @CurrentUser Account account){
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
        }
    }

}
