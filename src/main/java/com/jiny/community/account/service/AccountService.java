package com.jiny.community.account.service;


import com.jiny.community.settings.controller.NotificationForm;
import com.jiny.community.settings.controller.Profile;
import com.jiny.community.account.controller.dto.SignUpForm;
import com.jiny.community.account.domain.Account;

import com.jiny.community.account.domain.UserAccount;
import com.jiny.community.account.infra.mail.EmailMessage;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import java.util.Optional;


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
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = Optional.ofNullable(accountRepository.findByEmail(username))
                                    .orElse(accountRepository.findByNickname(username));
        if(account ==null){
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
    }

    public void sendVerificationEmail(Account newAccount){
//        Context context = new Context();
//        context.setVariable("link", String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(),
//                newAccount.getEmail()));
//        context.setVariable("nickname", newAccount.getNickname());
//        context.setVariable("linkName", "이메일 인증하기");
//        context.setVariable("message", "가입 인증을 위해 링크를 클릭하세요.");
//        context.setVariable("host", appProperties.getHost());
//        String message = templateEngine.process("mail/simple-link", context);
//        emailService.sendEmail(EmailMessage.builder()
//                .to(newAccount.getEmail())
//                .subject("회원 가입 인증")
//                .message(message) //정보를 넘기고 sendEmail에서 추가 정보 세팅
//                .token(newAccount.getEmailToken())
//                .build());
        threadLocal.set(appProperties.getHost()+"/check-email-token?token="+newAccount.getEmailToken()+"&email="+newAccount.getEmail());

    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserAccount(account),
                account.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
        //회원 정보를 token으로 만들어 Authentication 저장
        //메일에 get으로 보내는 url에 토큰을 실어 보내고
        //해당 get요청으로 토큰을 오면 Authentication저장된 토큰과 비교해서 isValid 값을 true로 변경
    }

    public void verify(Account account) {
        account.verified();
        login(account);
    }

    public void updateProfile(Account account, Profile profile){

        account.updateProfile(profile);
        log.debug("updateProfile={}, {}, {}",profile.getBio(),profile.getCompany(),profile.getImage());
        accountRepository.save(account);
    }

    public void updatePassword(Account account,String newPassword){
        account.updatePassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotification(Account account, NotificationForm notificationForm) {
        account.updateNotification(notificationForm);
        accountRepository.save(account);
    }

    public void sendLoginLink(Account account){
        account.createRandomToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("비밀번호 찾기 이메일 로그인");
        mailMessage.setText("/login-by-email?token="+account.getEmailToken()+"&email="+account.getEmail());
        mailMessage.setFrom(email);
        javaMailSender.send(mailMessage);
    }
}
