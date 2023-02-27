package com.jiny.community.notification.repository;

import com.jiny.community.account.domain.Account;
import com.jiny.community.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Long countByAccountAndChecked(Account account, boolean checked);

    @Transactional
    List<Notification> findByAccountAndCheckedOrderByCreatedDesc(Account account,boolean b);

    @Transactional
    void deleteByAccountAndChecked(Account account,boolean b);
}
