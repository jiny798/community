package com.jiny.community.account.controller;

import com.jiny.community.account.controller.validator.SignUpFormValidator;
import com.jiny.community.account.domain.Account;
import com.jiny.community.account.controller.dto.SignUpForm;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final UserService userService;

    @GetMapping(value = "/account/new")
    public String createForm(Model model){
        model.addAttribute("form", new SignUpForm());
        return "account/createAccountForm";
    }

    @PostMapping(value = "/account/new")
    public String create(@Validated @ModelAttribute("form") SignUpForm form, BindingResult result){
        log.info("회원가입 요청");
        if(!form.getPassword().equals(form.getPassword2())){
            result.reject("notEqualsPassword");
        }
        signUpFormValidator.validate(form,result);
        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "account/createAccountForm";
        }

        Account account = accountService.signUp(form);
        accountService.login(account);

        return "redirect:/";
    }

    @GetMapping("/check-email")
    public String checkMail(@CurrentUser Account account,Model model){
        model.addAttribute("email",account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-email")
    public String resendEmail(@CurrentUser Account account,Model model){
        if(!account.enableSendEmail()){
            model.addAttribute("error","5분 후 다시 시도해주세요.");
            model.addAttribute("email",account.getEmail());
            return "account/check-email";
        }
        accountService.sendVerificationEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname,Model model,@CurrentUser Account account ){
        Account findAccount = accountRepository.findByNickname(nickname);
        if(findAccount ==null){
           throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        model.addAttribute(findAccount);
        model.addAttribute("isOwner", findAccount.equals(account)); // (3)
        return "account/profile";
    }


}
