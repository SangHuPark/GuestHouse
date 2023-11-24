package project.GuestHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.GuestHouse.domain.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
