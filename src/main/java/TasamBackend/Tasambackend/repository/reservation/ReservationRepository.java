package TasamBackend.Tasambackend.repository.reservation;

import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);


    //List<Reservation> findAllByReservationDate(LocalDate date);
    List<Reservation> findAllByReservationDate(LocalDate reservationDate);

    List<Reservation> findAllByUser(User user);

    List<Reservation> findByTitleContaining(String keyword);
}
