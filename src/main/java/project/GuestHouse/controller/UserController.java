package project.GuestHouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.GuestHouse.domain.dto.Response;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserJoinRequest;
import project.GuestHouse.domain.dto.user.UserJoinResponse;
import project.GuestHouse.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto join(@RequestBody UserJoinRequest userJoinRequest) throws Exception {
        return userService.join(userJoinRequest);
    }

}
