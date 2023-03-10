package com.jiny.community.notification.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.notification.domain.Notification;
import com.jiny.community.notification.dto.NotificationResponseDto;
import com.jiny.community.notification.repository.NotificationRepository;
import com.jiny.community.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotifications(@CurrentUser Account account, Model model){
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDesc(account,false);
        long numberOfChecked = notificationRepository.countByAccountAndChecked(account,true);

        putNotificationsByType(model,notifications,numberOfChecked,notifications.size());
        model.addAttribute("isNew",false);

        List<Notification> readNotifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDesc(account,true);
        model.addAttribute("readNotifications",readNotifications);
        //읽은 알림 readNotifications 을 먼저 보내고, 읽음 처리
        notificationService.markAsRead(notifications);
        return "notification/list";
    }

//    @GetMapping("/notifications/old")
//    public String getOldNotifications(@CurrentUser Account account, Model model){
//        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDesc(account,true);
//        long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account,false);
//
//        putNotificationsByType(model,notifications,notifications.size(),numberOfNotChecked);
//        model.addAttribute("isNew",false);
//        return "notification/list";
//    }

    @DeleteMapping("/notifications")
    public String deleteNotifications(@CurrentUser Account account){
        notificationRepository.deleteByAccountAndChecked(account,true);
        return "redirect:/notifications";
    }

    private void putNotificationsByType(Model model,List<Notification> notifications , long numberOfChecked, long numberOfNotChecked){
        ArrayList<NotificationResponseDto> newCommentNotifications = new ArrayList<>();
        for (Notification notification : notifications){
            switch (notification.getNotificationType()){
                case COMMENT_CREATED:{
                    NotificationResponseDto notificationDto = new NotificationResponseDto(notification.getId(),
                            notification.getTitle(),notification.getLink(),notification.getMessage(),notification.isChecked(),notification.getCreated(),
                            notification.getNotificationType());
                    newCommentNotifications.add(notificationDto);
                }
            }
        }
        model.addAttribute("numberOfNotChecked",numberOfNotChecked);
        model.addAttribute("numberOfChecked",numberOfChecked);
        model.addAttribute("notifications",notifications);
        model.addAttribute("newCommentNotifications",newCommentNotifications);
    }

}
