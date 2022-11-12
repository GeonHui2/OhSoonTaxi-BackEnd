package TasamBackend.Tasambackend.repository.user;

import TasamBackend.Tasambackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);
    User save(User user);

    boolean existsByUid(String uid);
}
