package com.jiny.community.dto.account;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountForm {
    @NotEmpty(message = "이메일은 필수 입니다")
    private String email;
    private String nickname;
    private String password;

}
