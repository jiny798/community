package com.jiny.community.board.event;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.CommonAttribute;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.domain.Post;
import com.jiny.community.account.infra.mail.EmailMessage;
import com.jiny.community.account.infra.mail.EmailService;
import com.jiny.community.notification.domain.Notification;
import com.jiny.community.notification.domain.NotificationType;
import com.jiny.community.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Slf4j
@Async
@Transactional
@Component @RequiredArgsConstructor
public class CommentEventListener {

    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    @EventListener
    public void handleStudyCreatedEvent(CommentCreatedEvent commentCreatedEvent){
        Comment comment = commentCreatedEvent.getComment();
        Post findpost = comment.getPost(); // 커멘트가 등록된 post 조회
        log.info("COMMENT IS CREATED = "+comment.getContent()+" id= "+comment.getId());
        //TODO NOTIFICATION 전달
        Account account = accountRepository.findById(findpost.getAccount().getId()).get(); //조회된 post의 주인 조회

            CommonAttribute.NotificationSetting notificationSetting = account.getNotificationSetting();

            if(notificationSetting.isCommentCreatedByEmail()){ //계정 이메일 알림 설정 확인
                sendEmail(comment,account);
            }
            if(notificationSetting.isCommentCreatedByWeb()){ //계정 웹 알림 설정 확인
                saveNotification(comment,account);
            }

    }


    private void sendEmail(Comment comment, Account account){
        Context context = new Context();
        context.setVariable("link", "/post/" + comment.getPost().getId());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", comment.getContent());
        context.setVariable("message", "댓글이 달렸습니다.");
        context.setVariable("host", "localhost:8080");
        String message = templateEngine.process("mail/simple-link", context);
        emailService.sendEmail(EmailMessage.builder()
                .to(account.getEmail())
                .subject("댓글이 달렸습니다.")
                .message(message)
                .build());
    }


    private void saveNotification(Comment comment,Account account){
        log.debug("notification sava ={}, {}",comment.getContent(),account.getNickname());
        notificationRepository.save(Notification.from("댓글 알림",
                "/post/"+comment.getPost().getId(),
                false,
                LocalDateTime.now(),
                comment.getContent(),
                account,
                NotificationType.COMMENT_CREATED));
    }
}
