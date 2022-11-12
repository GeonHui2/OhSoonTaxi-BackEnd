package TasamBackend.Tasambackend.repository.user;

import TasamBackend.Tasambackend.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String token);

    RefreshToken findByRefreshToken(String token);

    @Transactional
    void deleteByRefreshToken(String token);
}
