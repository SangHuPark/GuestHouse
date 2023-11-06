package project.GuestHouse.domain.dto.user;

import lombok.Getter;
import lombok.Setter;
import project.GuestHouse.domain.entity.User;

import java.time.LocalDate;

@Getter @Setter
public class UserSearchResponse {
    private Long id;
    private String email;
    private String userName;
    private LocalDate birth;
    private String nickname;
    private String phoneNum;

    public UserSearchResponse(User user) {
        id = user.getId();
        email = user.getEmail();
        userName = user.getUserName();
        birth = user.getBirth();
        nickname = user.getNickname();
        phoneNum = user.getPhoneNum();
    }
}
