package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.*;
import project.GuestHouse.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getEmail(), userDto.getNickname()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest);
        return Response.success(new UserLoginResponse(token));
    }

}
