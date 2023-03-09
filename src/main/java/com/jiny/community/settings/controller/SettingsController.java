package com.jiny.community.settings.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.common.controller.common.NotFoundException;
import com.jiny.community.settings.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SettingsController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @GetMapping("/settings/password")
    public String passwordUpdateForm(@CurrentUser Account account,Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return "account/settings/password";
    }

    @PostMapping("/settings/password")
    public String updatePassword(@CurrentUser Account account,
                                 @Validated @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult result, Model model, RedirectAttributes attributes){
        log.debug("password 변경 {}",passwordForm.getNewPassword());
        if(result.hasErrors()){
            model.addAttribute(account);
            return "account/settings/password";
        }
        accountService.updatePassword(account,passwordForm.getNewPassword());
        attributes.addFlashAttribute("message","변경 완료");
        return "redirect:"+"/settings/password";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentUser Account account ){
        Account findAccount = accountRepository.findByNickname(nickname);
        if(findAccount ==null){
            throw new NotFoundException(HttpStatus.BAD_REQUEST,"사용자를 찾을 수 없습니다.");
        }
        model.addAttribute(findAccount);
        model.addAttribute("isOwner", findAccount.equals(account));
        return "account/profile";
    }

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(Profile.from(account));
        return "account/settings/profile";
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@CurrentUser Account account,
                                @Valid @ModelAttribute("profile") Profile profile, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "account/settings/profile";
        }
        accountService.updateProfile(account,profile);
        return "redirect:/profile/"+account.getNickname();
    }

    @GetMapping("/settings/notification")
    public String notificationForm(@CurrentUser Account account,Model model){
        model.addAttribute(account);
        model.addAttribute(NotificationForm.from(account));
        return "account/settings/notification";
    }

    @PostMapping("/settings/notification")
    public String updateNotification(@CurrentUser Account account, @Valid NotificationForm notificationForm, Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "account/settings/notification";
        }
        model.addAttribute(account);

        accountService.updateNotification(account, notificationForm);
        attributes.addFlashAttribute("message", "알림설정을 수정하였습니다.");
        return "redirect:" + "/settings/notification";
    }
}
