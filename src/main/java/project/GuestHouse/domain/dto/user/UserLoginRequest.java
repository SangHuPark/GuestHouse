package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserLoginRequest {

    @NotBlank(message = "올바른 형식의 ID가 아닙니다.")
    private String email;

    @NotBlank(message = "올바른 형식의 비밀번호가 아닙니다.")
    private String password;
}
