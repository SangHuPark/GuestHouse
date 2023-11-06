package project.GuestHouse.domain.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserLoginRequest {

    @NotBlank(message = "올바른 형식의 ID가 아닙니다.")
    private String email;

    @NotBlank(message = "올바른 형식의 비밀번호가 아닙니다.")
    private String password;
}
