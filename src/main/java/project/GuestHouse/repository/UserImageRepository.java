package project.GuestHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.domain.entity.UserImage;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Optional<UserImage> findByUserId(Long userId);
}
