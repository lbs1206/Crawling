package com.hanil.fluxus.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLogin {

    @Email
    @NotBlank(message = "이메일 항목은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호 항목은 필수 입니다.")
    private String password;

}
