package com.jiny.community.infra.mail;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.infra.mail.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;
    private AccountRepository accountRepository;
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            log.debug("emailMessage ={}, {}, {}",emailMessage.getTo(),emailMessage.getSubject()
            ,emailMessage.getTo());
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            mimeMessageHelper.setFrom(email);

            javaMailSender.send(mimeMessage);
//            log.info("sent email: {}", emailMessage.getMessage());
//            SimpleMailMessage mailMessage = new SimpleMailMessage(); // (7)
//            mailMessage.setTo(emailMessage.getTo());
//            mailMessage.setSubject("회원 가입 인증");
//
//            mailMessage.setText(String.format("/check-email-token?token=%s&email=%s", emailMessage.getToken(),
//                    emailMessage.getTo()));
//              javaMailSender.send(mailMessage);
        } catch (Exception e) { //MessagingException e
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}
