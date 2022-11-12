package TasamBackend.Tasambackend.repository.participation;

import TasamBackend.Tasambackend.entity.Participation;
import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Participation save(Participation participation);

    Optional<Participation> findById(Long id);
    @Transactional
    void deleteById(Long participationId);
    List<Participation> findAllByReservationId(Long reservationId);
    List<Participation> findAllByUser(User user);

    Optional<Participation> findByUserAndReservation(User user, Reservation reservation);
}
