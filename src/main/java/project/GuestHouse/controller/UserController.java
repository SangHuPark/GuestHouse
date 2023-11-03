package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.*;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinRequest userJoinRequest, BindingResult bindingResult) {
        bindingResultErrorsCheck(bindingResult);
        UserDto userDto = userService.join(userJoinRequest);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원가입 완료")
                .body(new UserJoinResponse(userDto.getEmail(), userDto.getNickname())).build(), HttpStatus.OK);
        // return Response.success("회원가입 완료", new UserJoinResponse(userDto.getEmail(), userDto.getNickname());
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
        List<User> users = userService.findAllUser();
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("전체 회원 조회 완료")
                .body(users).build(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchUser(@Valid @RequestBody String email) {
        User user = userService.findUserByEmail(email);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원 조회 완료")
                .body(user).build(), HttpStatus.OK);
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePasswordById(@Valid @RequestBody Long id, @Valid @RequestBody String password) {
        userService.updateUserPassword(id, password);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("비밀번호 변경 완료")
                .body(password).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(Response.builder()
                .isSuccess(true)
                .message("회원 탈퇴 완료").build(), HttpStatus.OK);
    }

    private void bindingResultErrorsCheck(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }
    }
}
