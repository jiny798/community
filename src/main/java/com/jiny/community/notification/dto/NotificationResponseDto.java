package com.jiny.community.notification.dto;

import com.jiny.community.account.domain.Account;
import com.jiny.community.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String link;
    private String message;
    private boolean checked;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
