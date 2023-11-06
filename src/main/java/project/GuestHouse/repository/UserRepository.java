package project.GuestHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.GuestHouse.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이메일로 조회
    Optional<User> findByEmail(String email);

    // 사용자 pk 로 조회
    Optional<User> findById(Long id);
}
