package com.jiny.community.controller.account;

import com.jiny.community.domain.Account;
import com.jiny.community.dto.user.SignUpForm;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.service.AccountService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping(value = "/new")
    public String createForm(Model model){
        model.addAttribute("form", new SignUpForm());
        return "account/createAccountForm";
    }

    @PostMapping(value = "/new")
    public String create(@Validated @ModelAttribute("form") SignUpForm form, BindingResult result){
        log.info("회원가입 요청");
        if(!form.getPassword().equals(form.getPassword2())){
            result.reject("reconfirmPassword");
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "account/createAccountForm";
        }
        Account account = accountService.signUp(form);
        accountService.login(account);

        return "admin/board";
    }

}
