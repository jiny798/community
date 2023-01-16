package com.jiny.community.service;

import com.jiny.community.controller.SignUpForm;
import com.jiny.community.domain.Account;
import com.jiny.community.domain.UserAccount;
import com.jiny.community.infra.mail.AppProperties;
import com.jiny.community.infra.mail.EmailMessage;
import com.jiny.community.infra.mail.EmailService;
import com.jiny.community.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    public Account signUp(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendVerificationEmail(newAccount);
        return newAccount;
    }
    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.createAccount(signUpForm.getEmail(), signUpForm.getNickname(), passwordEncoder.encode(signUpForm.getPassword()));
        account.createRandomToken();
        return accountRepository.save(account);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByNickname(username);
        if(account ==null){
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
        // User를 구현한 UserAccount를 리턴
        // User는 Userdetails를 상속받음
    }

    public void sendVerificationEmail(Account newAccount){
        Context context = new Context();
        context.setVariable("link", String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(),
                newAccount.getEmail()));
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "Webluxible 가입 인증을 위해 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);
        emailService.sendEmail(EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("Webluxible 회원 가입 인증")
                .message(message)
                .build());
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserAccount(account),
                account.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
        //회원 정보를 token으로 만들어 Authentication 저장
        //메일에 get으로 보내는 url에 토큰을 실어 보내고
        //해당 get요청으로 토큰을 오면 Authentication저장된 토큰과 비교
    }

    public void verify(Account account) {
        account.verified();
        login(account);
    }

}
