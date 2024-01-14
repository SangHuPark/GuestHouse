package project.GuestHouse.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UserFailResponse {
    private final String errorMessage;
}
