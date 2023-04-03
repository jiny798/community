package com.jiny.community.account.infra.mail;

import com.jiny.community.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


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


        } catch (Exception e) { //MessagingException e
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }





}
