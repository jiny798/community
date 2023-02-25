package com.jiny.community.settings.controller;

import com.jiny.community.WithAccount;
import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("알림 설정 수정 폼 GET")
    @WithAccount("jyoung123")
    void notificationForm() throws Exception{
        mockMvc.perform(get("/settings/notification"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/notification"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notificationForm"));
    }

    @Test
    @DisplayName("알림 설정 수정 요청 POST")
    @WithAccount("jyoung123")
    void updateNotification() throws Exception{
        mockMvc.perform(post("/settings/notification")
                .param("commentCreatedByEmail", "true")
                .param("commentCreatedByWeb", "true")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/notification"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("jyoung123");
        assertTrue(account.getNotificationSetting().isCommentCreatedByEmail());
        assertTrue(account.getNotificationSetting().isCommentCreatedByWeb());

    }
}