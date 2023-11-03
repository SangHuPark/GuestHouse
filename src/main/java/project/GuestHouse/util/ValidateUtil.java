package project.GuestHouse.util;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.exception.ErrorCode;
import project.GuestHouse.exception.GuestException;
import project.GuestHouse.repository.UserRepository;

@Component
public class ValidateUtil {

    private final UserRepository userRepository;

    @Autowired
    @Builder
    public ValidateUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User validateUser(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GuestException(ErrorCode.NICKNAME_NOT_FOUND));
    }
}
