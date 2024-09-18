package com.example.demo.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @NotEmpty(message = "아이디가 비어있습니다.")
    @Size(min = 3, max = 25, message = "아이디의 제한 길이는 3이상 25이하 입니다.")
    private String username;

    @NotEmpty(message = "비밀번호가 비어있습니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인이 비어있습니다.")
    private String passwordCheck;

    @NotEmpty(message = "닉네임이 비어있습니다.")
    private String nickname;
}
