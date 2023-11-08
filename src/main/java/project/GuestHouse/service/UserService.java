package project.GuestHouse.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.GuestHouse.domain.dto.user.*;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.exception.ErrorCode;
import project.GuestHouse.exception.GuestException;
import project.GuestHouse.auth.JwtTokenProvider;
import project.GuestHouse.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Long createUser(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail())
                .ifPresent(user -> {
                    throw new GuestException(ErrorCode.DUPLICATED_USER_EMAIL);
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));

        return savedUser.getId();
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new GuestException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return new UserLoginResponse(email, token);
    }

    public List<UserSearchResponse> findAllUser() {
        List<User> users = userRepository.findAll();
        List<UserSearchResponse> userList = users.stream()
                .map(UserSearchResponse::new)
                .collect(Collectors.toList());
        return userList;
    }

    public UserSearchResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_EMAIL_NOT_FOUND));
        return new UserSearchResponse(user);
    }

    public boolean updateUserPassword(Long id, String password) {
        Optional<User> result = userRepository.findById(id);
        // null 이 아니면 get()의 결과가 넘어온다.
        User user = result.orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        user.updatePassword(encoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        Optional<User> result = userRepository.findById(id);
        User user = result.orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        userRepository.delete(user);
        return true;
    }
}