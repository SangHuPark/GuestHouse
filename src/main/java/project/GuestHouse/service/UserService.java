package project.GuestHouse.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.GuestHouse.domain.dto.user.UserDto;
import project.GuestHouse.domain.dto.user.UserJoinRequest;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail())
                .ifPresent(user -> {

                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(userJoinRequest.getPassword()));

        return savedUser.toDto();
    }
}
