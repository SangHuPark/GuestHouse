package project.GuestHouse.domain.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class UserLoginRequest {

    @NotBlank(message = "잘못된 데이터 형식입니다.")
    @Email(message = "올바른 형식의 이메일이 아닙니다.")
    private String email;

    @NotBlank(message = "잘못된 데이터 형식입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16 길이의 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;
}
