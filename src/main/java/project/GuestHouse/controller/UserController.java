package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserJoinRequest;
import project.GuestHouse.domain.dto.user.UserJoinResponse;
import project.GuestHouse.domain.dto.user.UserLoginRequest;
import project.GuestHouse.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto join(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        return userService.join(userJoinRequest);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }

}
