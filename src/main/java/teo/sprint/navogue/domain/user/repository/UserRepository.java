package teo.sprint.navogue.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.sprint.navogue.domain.user.data.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
