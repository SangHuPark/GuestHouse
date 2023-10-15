package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserSignUpRequest;
import project.GuestHouse.domain.dto.user.UserSignUpResponse;
import project.GuestHouse.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserSignUpResponse> join(@RequestBody UserSignUpRequest userSignUpRequest) throws Exception {
        return userService
    }

}
