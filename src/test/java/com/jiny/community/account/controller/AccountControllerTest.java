package com.jiny.community.account.controller;

import com.jiny.community.account.controller.dto.SignUpForm;
import com.jiny.community.account.service.AccountService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.UrlAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @Autowired
    EntityManager em;

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("회원가입 오류 - 비밀번호 불일치")
    void equalsPasswordFail() throws Exception {
        mockMvc.perform(post("/account/new")
                .param("email","jiny@jiny.com")
                .param("nickname","jiny1234")
                .param("password","jiny1234!")
                .param("password2","jiny1234!!").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> {
//                    System.out.println("abc - "+result.getModelAndView().getModel().get("org.springframework.validation.BindingResult.form"));

                    BeanPropertyBindingResult beanPropertyBindingResult =
                            (BeanPropertyBindingResult)result.getModelAndView().getModel().get("org.springframework.validation.BindingResult.form");

                    Assertions.assertThat(beanPropertyBindingResult.getFieldError().getDefaultMessage()).isEqualTo("패스워드가 서로 일치하지 않습니다.");

                });



    }

    @Test
    @DisplayName("이메일 로그인 성공 처리")
    void loginByEmail() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("jiny@jiny.com");
        signUpForm.setPassword("jiny1234!");
        signUpForm.setPassword2("jiny1234!");
        signUpForm.setNickname("jiny");

        accountService.signUp(signUpForm);


        mockMvc.perform(post("/login")
                    .param("username","jiny@jiny.com")
                    .param("password","jiny1234!")
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated().withUsername("jiny"));

    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        mockMvc.perform(post("/logout") // (1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")) // (2)
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // (3)
    }
}