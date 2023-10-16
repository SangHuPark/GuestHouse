package project.GuestHouse.domain.dto.user;

import lombok.*;
import project.GuestHouse.domain.entity.ProviderType;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.domain.entity.UserType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class UserJoinRequest {

    @NotBlank(message = "올바른 형식의 이메일이 아닙니다.")
    @Email
    private String email;

    @NotBlank(message = "올바른 형식의 비밀번호가 아닙니다.")
    private String password;

    @NotBlank(message = "올바른 형식의 이름이 아닙니다.")
    @Size(min = 2, message = "이름은 2자 이상 입력하세요.")
    private String name;

    @NotBlank()
    private LocalDateTime birth;

    @NotBlank(message = "올바른 형식의 닉네임이 아닙니다.")
    @Size(min = 2, max = 10, message = "닉네임의 길이는 2자 이상 10자 이하로 입력하세요.")
    private String nickname;

    private int phoneNum;

    public User toEntity(String password) {
        return User.builder()
                .email(this.email)
                .password(password)
                .name(this.name)
                .birth(this.birth)
                .nickname(this.nickname)
                .phoneNum(this.phoneNum)
                .provider(ProviderType.LOCAL) // default = LOCAL
                .userType(UserType.NORMAL) // default = NORMAL
                .build();
    }
}
