package com.jiny.community.notification.repository;

import com.jiny.community.account.domain.Account;
import com.jiny.community.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Long countByAccountAndChecked(Account account, boolean checked);


    List<Notification> findByAccountAndCheckedOrderByCreatedDesc(Account account,boolean b);


    void deleteByAccountAndChecked(Account account,boolean b);
}
