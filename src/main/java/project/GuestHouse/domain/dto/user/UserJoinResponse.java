package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinResponse {

    private String userEmail;
    private String nickname;
}
