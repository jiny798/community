package com.jiny.community;

import com.jiny.community.account.controller.dto.SignUpForm;
import com.jiny.community.account.service.AccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class AccountWithSecurityFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;
    public AccountWithSecurityFactory(AccountService accountService){
        this.accountService = accountService;
    }
    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        String nickname = annotation.value();

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname(nickname);
        signUpForm.setEmail(nickname+ "@naver.com");
        signUpForm.setPassword("abcde123!");
        signUpForm.setPassword2("abcde123!");
        accountService.signUp(signUpForm);

        UserDetails principal = accountService.loadUserByUsername(nickname);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal,principal.getPassword(),principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
