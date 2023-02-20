package com.jiny.community.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserService userService;
    @GetMapping("/check-email-token")
    public String verifyEmail(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return "account/email-verification";
        }
        if (!token.equals(account.getEmailToken())) {
            model.addAttribute("error", "wrong.token");
            return "account/email-verification";
        }
        accountService.verify(account);
        model.addAttribute("numberOfUsers", 100);
        model.addAttribute("nickname", account.getNickname());
        return "account/email-verification";
    }





}
