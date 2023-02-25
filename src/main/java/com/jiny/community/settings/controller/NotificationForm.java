package com.jiny.community.settings.controller;

import com.jiny.community.account.domain.Account;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationForm {
    private boolean commentCreatedByEmail;
    private boolean commentCreatedByWeb;


    protected NotificationForm(Account account) {
        this.commentCreatedByEmail = account.getNotificationSetting().isCommentCreatedByEmail();
        this.commentCreatedByWeb = account.getNotificationSetting().isCommentCreatedByWeb();
    }

    public static NotificationForm from(Account account) {
        return new NotificationForm(account);
    }
}
