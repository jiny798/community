package com.jiny.community.settings.controller;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordForm {
    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&+]{8,20}$",message = "문자,숫자 포함하여 8~20자를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[@$!%*#?&+]).{8,20}$",message = "특수기호 @$!%*#?&+ 중 하나가 포함되어야 합니다.")
    private String newPassword;
    @Length(min = 8, max = 50)
    private String newPasswordConfirm;
}
