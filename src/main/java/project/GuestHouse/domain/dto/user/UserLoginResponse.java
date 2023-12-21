package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLoginResponse {
    private String email;
    private String token;
    private String imageUrl;

    public UserLoginResponse(String email, String token, String imageUrl) {
        this.email = email;
        this.token = token;
        this.imageUrl = imageUrl;
    }
}
