package project.GuestHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.GuestHouse.domain.entity.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

}
