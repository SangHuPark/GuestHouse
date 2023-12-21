package project.GuestHouse.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.GuestHouse.domain.dto.user.*;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.domain.entity.UserImage;
import project.GuestHouse.exception.ErrorCode;
import project.GuestHouse.exception.GuestException;
import project.GuestHouse.auth.JwtTokenProvider;
import project.GuestHouse.repository.UserImageRepository;
import project.GuestHouse.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserJoinResponse createUser(UserJoinRequest userJoinRequest) {
        //        LocalDate birth = LocalDate.parse(userJoinRequest.getBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate birth = LocalDate.parse("2000-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(userRepository.findByPhoneNum(userJoinRequest.getPhoneNum()) != null)
            throw new GuestException(ErrorCode.DUPLICATED_USER_PHONENUM);

        userRepository.save(
            userJoinRequest.toEntity(
                    encoder.encode(userJoinRequest.getPassword()),
                    birth));

        String imageUrl = "https://invitbucket.s3.ap-northeast-2.amazonaws.com/defaultUserProfileImage.svg";

        return new UserJoinResponse(userJoinRequest.getEmail(), userJoinRequest.getPassword(), imageUrl);
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_EMAIL_NOT_FOUND));

        String imageUrl = "https://invitbucket.s3.ap-northeast-2.amazonaws.com/defaultUserProfileImage.svg";

        if (userImageRepository.findByUserId(user.getId()).isPresent()) {
            Optional<UserImage> userImage = userImageRepository.findByUserId(user.getId());
            imageUrl = userImage.get().getAccessUrl();
        }

        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new GuestException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return new UserLoginResponse(email, token, imageUrl);
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

    public Boolean duplicateUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.isEmpty();
    }

    public void updateUserPassword(Long id, String password) {
        // null 이 아니면 get()의 결과가 넘어온다.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        user.updatePassword(encoder.encode(password));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GuestException(ErrorCode.USER_ID_NOT_FOUND));
        user.delete();
    }

}