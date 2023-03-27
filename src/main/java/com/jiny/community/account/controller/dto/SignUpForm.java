package com.jiny.community.account.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {
    @Email(message = "이메일 형식이 맞는지 확인해주세요")
    @NotEmpty(message = "이메일은 필수 입니다")
    private String email;

    @NotBlank
    @Length(min = 3, max = 20,message = "길이는 3~20자 사이여야 합니다")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "문자,숫자만 입력 가능합니다")
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&+]{8,20}$",message = "문자,숫자 포함하여 8~20자를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[@$!%*#?&+]).{8,20}$",message = "특수기호 @$!%*#?&+ 중 하나가 포함되어야 합니다.") // . 뒤에 범위랑
    private String password;

    private String password2;

}
