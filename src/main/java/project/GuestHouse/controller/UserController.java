package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.*;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.service.EmailService;
import project.GuestHouse.service.S3Service;
import project.GuestHouse.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;
    private final EmailService emailService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> join(@ModelAttribute @Valid UserJoinRequest userJoinRequest, BindingResult bindingResult) throws IOException {
        bindingResultErrorsCheck(bindingResult);

        User user = userService.createUser(userJoinRequest);

        String imageUrl = "https://invitbucket.s3.ap-northeast-2.amazonaws.com/defaultUserProfileImage.svg";
        if (userJoinRequest.getProfileImg() != null)
            imageUrl = s3Service.saveImage(userJoinRequest.getProfileImg(), user);

        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원가입 완료")
                .body("image_url: " + imageUrl).build(), HttpStatus.OK);
        // return Response.success("회원가입 완료", new UserJoinResponse(userDto.getEmail(), userDto.getNickname());
    }

    @PostMapping("/mail/confirm")
    public ResponseEntity<?> mailConfirm(@Valid @RequestBody HashMap<String, String> request) {
        String email = request.get("email");
        Boolean result = userService.duplicateUserByEmail(email);

        if (result) {
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(true)
                    .message("사용 가능한 이메일 입니다.")
                    .body("user_email: " + email).build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("이미 사용 중인 이메일 입니다.")
                    .body("user_email: " + email).build(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/mail/send")
    public ResponseEntity<?> mailSend(@Valid @RequestBody HashMap<String, String> request) throws Exception {
        String email = request.get("email");
        String code = emailService.sendSimpleMessage(email);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("인증번호 전송 성공")
                .body("confirm_code: " + code).build(), HttpStatus.OK);
    }

    @PostMapping("/mail/code")
    public ResponseEntity<?> codeConfirm(@Valid @RequestBody HashMap<String, String> request) throws Exception {
        String key = request.get("email");
        String code = request.get("code");
        Boolean confirmEmail = emailService.verifyEmail(key, code);

        if (confirmEmail) {
            emailService.deleteEmail(key);
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(true)
                    .message("인증번호가 일치합니다.")
                    .body(key).build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("인증번호가 일치하지 않습니다.")
                    .body(key).build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult bindingResult) {
        try {
            bindingResultErrorsCheck(bindingResult);
            UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(true)
                    .message("로그인 성공")
                    .body(userLoginResponse).build(), HttpStatus.OK);
            // return Respose.success("로그인 성공", userLoginResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("로그인 실패")
                    .body(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchAllUser() {
        List<UserSearchResponse> users = userService.findAllUser();
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("전체 회원 조회 완료")
                .body(users).build(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchUser(@Valid @RequestBody HashMap<String, String> request) {
        String email = request.get("email");
        UserSearchResponse user = userService.findUserByEmail(email);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원 조회 완료")
                .body(user).build(), HttpStatus.OK);
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePasswordById(@Valid @RequestBody HashMap<String, String> request) {
        Long id = Long.valueOf(request.get("id"));
        String password = request.get("password");
        userService.updateUserPassword(id, password);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("비밀번호 변경 완료")
                .body("updated_pw = " + password).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원 탈퇴 완료").build(), HttpStatus.OK);
    }

    private ResponseEntity<?> bindingResultErrorsCheck(BindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
//            throw new GuestException(ErrorCode.INTERNAL_SERVER_ERROR, errorMap.toString());
        }
        return new ResponseEntity<>(Response.builder()
                .isSuccess(false)
                .message("Internal Server Error")
                .body("error = " + errorMap.toString()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
