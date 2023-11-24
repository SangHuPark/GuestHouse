package project.GuestHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.GuestHouse.domain.entity.User;

import java.util.Optional;

// JpaRepository 에 @Repository 로 빈에 등록하고 있어 별도의 어노테이션이 필요 없음
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이메일 로 조회
    Optional<User> findByEmail(String email);

    // 사용자 pk 로 조회
    Optional<User> findById(Long id);
}
