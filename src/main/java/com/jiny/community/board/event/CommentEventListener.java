package com.jiny.community.board.event;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.CommonAttribute;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.domain.Post;
import com.jiny.community.infra.mail.EmailMessage;
import com.jiny.community.infra.mail.EmailService;
import com.jiny.community.notification.domain.Notification;
import com.jiny.community.notification.domain.NotificationType;
import com.jiny.community.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Slf4j
@Async
@Transactional(readOnly = true)
@Component @RequiredArgsConstructor
public class CommentEventListener {

    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    @EventListener
    public void handleStudyCreatedEvent(CommentCreatedEvent commentCreatedEvent){
        Comment comment = commentCreatedEvent.getComment();
        Post findpost = comment.getPost();
        log.info("COMMENT IS CREATED = "+comment.getContent()+" id= "+comment.getId());
        //TODO NOTIFICATION 전달
        Account account = accountRepository.findById(findpost.getAccount().getId()).get();

            CommonAttribute.NotificationSetting notificationSetting = account.getNotificationSetting();
            if(notificationSetting.isCommentCreatedByEmail()){
                sendEmail(comment,account);
            }
            if(notificationSetting.isCommentCreatedByWeb()){
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
        notificationRepository.save(Notification.from("댓글 알림",
                "/post/"+comment.getPost().getId(),
                false,
                LocalDateTime.now(),
                comment.getContent(),
                account,
                NotificationType.COMMENT_CREATED));
    }
}
