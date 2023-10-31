package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {
    private String email;
    private String jwt;
}
