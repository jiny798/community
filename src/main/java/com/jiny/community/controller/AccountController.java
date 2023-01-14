package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import com.jiny.community.dto.account.AccountForm;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.service.AccountService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    private final UserService userService;

    @GetMapping(value = "/new")
    public String createForm(Model model){
        model.addAttribute("accountForm", new AccountForm());
        return "account/createAccountForm";
    }

    @PostMapping(value = "/new")
    public String create(@Valid AccountForm form, BindingResult result){
        log.info("회원가입 요청");
        if (result.hasErrors()) {
            return "account/createAccountForm";
        }
        Account account = new Account();
        account.setEmail(form.getEmail());
        account.setRole("USER");
        account.setNickname(form.getNickname());
        account.setPassword(form.getPassword());

        userService.join(account);
        return "redirect:/";
    }

}
