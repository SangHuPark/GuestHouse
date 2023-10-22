package project.GuestHouse.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserJoinRequest;
import project.GuestHouse.domain.dto.user.UserLoginRequest;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.exception.ErrorCode;
import project.GuestHouse.exception.GuestException;
import project.GuestHouse.jwt.JwtUtil;
import project.GuestHouse.repository.UserRepository;
import project.GuestHouse.util.ValidateUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidateUtil validateUtil;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.validateUtil = ValidateUtil.builder()
                .userRepository(userRepository)
                .build();
    }

    private long expireTimeMs = 1000 * 60 * 60; // 1시간

    @Value("${jwt.secret}")
    private String secretKey;

    public UserDto join(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail())
                .ifPresent(user -> {
                    throw new GuestException(ErrorCode.DUPLICATED_USER_EMAIL);
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));

        return savedUser.toDto();
    }

    public String login(UserLoginRequest userLoginRequest) {
        String userEmail = userLoginRequest.getEmail();

        User user = validateUtil.validateUser(userEmail);

        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new GuestException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtUtil.createToken(user.getNickname(), secretKey, expireTimeMs);
    }

    public User getUserByNickname(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GuestException(ErrorCode.NICKNAME_NOT_FOUND));
    }
}