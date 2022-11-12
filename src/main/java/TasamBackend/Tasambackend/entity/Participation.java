package TasamBackend.Tasambackend.entity;

import TasamBackend.Tasambackend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SpringBootApplication
public class Participation {

    @Id
    @GeneratedValue
    @Column(name = "participation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Integer seatPosition;

    @Builder
    public Participation(User user, Reservation reservation, Integer seatPosition) {
        this.user = user;
        this.reservation = reservation;
        this.seatPosition = seatPosition;
    }

    public void mappingReservation(Reservation reservation) {
        this.reservation=reservation;
    }

}
