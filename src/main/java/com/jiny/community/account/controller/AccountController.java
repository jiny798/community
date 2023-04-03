package com.jiny.community.account.controller;

import com.jiny.community.account.controller.validator.SignUpFormValidator;
import com.jiny.community.account.domain.Account;
import com.jiny.community.account.controller.dto.SignUpForm;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.account.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String create(@Validated @ModelAttribute("form") SignUpForm form, BindingResult result,RedirectAttributes redirectAttributes){
        log.info("회원가입 요청 = {}",form.getEmail());
        if(!form.getPassword().equals(form.getPassword2())){
            result.rejectValue("password","wrong.value","패스워드가 서로 일치하지 않습니다.");
        }
        signUpFormValidator.validate(form,result);
        if (result.hasErrors()) {
            log.debug("errors={}", result);
            return "account/createAccountForm";
        }

        Account account = accountService.signUp(form);
        accountService.login(account);

        //이메일 인증 임시
        String tempEmailToken = AccountService.threadLocal.get();
        log.debug("emailToken = " + tempEmailToken);
        AccountService.threadLocal.remove();

        redirectAttributes.addFlashAttribute("tempEmailToken",tempEmailToken);

        return "redirect:/";
    }

    @GetMapping("/check-email")
    public String checkMail(@CurrentUser Account account,Model model){
        model.addAttribute("email",account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-email")
    public String resendEmail(@CurrentUser Account account,Model model){
        if(!account.isEnableToSendEmail()){
            model.addAttribute("error","5분 후 다시 시도해주세요.");
            model.addAttribute("email",account.getEmail());
            return "account/check-email";
        }
        accountService.sendVerificationEmail(account);
        return "redirect:/";
    }

    @GetMapping("/email-login")
    public String emailLoginForm(){
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendLinkForEmailLogin(String email, Model model, RedirectAttributes attributes) { // (2)
        log.debug("이메일 전달 {}",email);
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }
        if (!account.isEnableToSendEmail()) {
            model.addAttribute("error", "너무 잦은 요청입니다. 5분 뒤에 다시 시도하세요.");
            return "account/email-login";
        }
        accountService.sendLoginLink(account);
        attributes.addFlashAttribute("message", "로그인 가능한 링크를 이메일로 전송하였습니다.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model) { // (3)
        Account account = accountRepository.findByEmail(email);
        if (account == null || !account.isValid(token)) {
            model.addAttribute("error", "로그인할 수 없습니다.");
            return "account/email-logged";
        }
        accountService.login(account);
        return "account/email-logged";
    }

}
