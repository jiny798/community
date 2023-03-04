package com.jiny.community.notification.service;

import com.jiny.community.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    public void markAsRead(List<Notification> notifications){
        notifications.forEach(Notification::read);
    }

}
