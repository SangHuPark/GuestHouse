package project.GuestHouse.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserJoinRequest;
import project.GuestHouse.domain.dto.user.UserLoginRequest;
import project.GuestHouse.domain.dto.user.UserLoginResponse;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.exception.ErrorCode;
import project.GuestHouse.exception.GuestException;
import project.GuestHouse.jwt.JwtTokenProvider;
import project.GuestHouse.repository.UserRepository;
import project.GuestHouse.util.ValidateUtil;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidateUtil validateUtil;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.validateUtil = ValidateUtil.builder()
                .userRepository(userRepository)
                .build();
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserDto createUser(UserJoinRequest userJoinRequest) {
        userRepository.findUserByEmail(userJoinRequest.getEmail())
                .ifPresent(user -> {
                    throw new GuestException(ErrorCode.DUPLICATED_USER_EMAIL);
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));

        return savedUser.toDto();
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        User user = validateUtil.validateUser(email);

        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new GuestException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return new UserLoginResponse(email, token);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_EMAIL_NOT_FOUND));
    }

    public boolean updateUserPassword(Long id, String password) {
        Optional<User> result = userRepository.findUserById(id);
        // null 이 아니면 get()의 결과가 넘어온다.
        User user = result.orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        user.updatePassword(password);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        Optional<User> result = userRepository.findUserById(id);
        User user = result.orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        userRepository.delete(user);
        return true;
    }
}