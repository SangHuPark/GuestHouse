package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLoginResponse {
    private String email;
    private String token;

    public UserLoginResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
