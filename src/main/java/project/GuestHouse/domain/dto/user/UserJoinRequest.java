package project.GuestHouse.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.GuestHouse.domain.entity.ProviderType;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.domain.entity.UserType;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter @Setter
public class UserJoinRequest {

    @NotBlank
    @Email(message = "올바른 형식의 이메일이 아닙니다.")
    private String email;

    @NotBlank(message = "올바른 형식의 비밀번호가 아닙니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16 길이의 영문, 숫자, 특수문자를 포함해야 합니다.")
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16 길이의 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "올바른 형식의 이름이 아닙니다.")
    @Size(min = 2, message = "이름은 2자 이상 입력하세요.")
    private String userName;

    @NotBlank(message = "올바른 형식의 닉네임이 아닙니다.")
    @Size(min = 2, max = 10, message = "닉네임의 길이는 2자 이상 10자 이하로 입력하세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10 자리여야 합니다.")
    private String userNickname;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String birth;

    private MultipartFile profileImg;

    private String phoneNum;

    public User toEntity(String password, LocalDate birth) {
        return User.builder()
                .email(this.email)
                .password(password)
                .userName(this.userName)
                .userNickname(this.userNickname)
                .birth(birth)
                .phoneNum(this.phoneNum)
                .provider(ProviderType.LOCAL) // default = LOCAL
                .userType(UserType.NORMAL) // default = NORMAL
                .build();
    }
}
