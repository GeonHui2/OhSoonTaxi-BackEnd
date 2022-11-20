package TasamBackend.Tasambackend.entity;

import TasamBackend.Tasambackend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private LocalDate reservationDate;
    @Column(nullable = false)
    private LocalTime reservationTime;
    @Column(nullable = false, length = 200)
    private String startingPlace;
    @Column(nullable = false, length = 200)
    private String destination;
    @Enumerated(STRING)
    @Column(nullable = false, length = 10)
    private Sex sex;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(STRING)
    private ReservationStatus reservationStatus;
    private Integer passengerNum;
    private Integer currentNum;
    @Column(nullable = false, length = 20)
    private String challengeWord;
    @Column(nullable = false, length = 20)
    private String countersignWord;
    @Column(nullable = false, length = 200)
    private Double startLatitude;
    @Column(nullable = false, length = 200)
    private Double startLongitude;
    @Column(nullable = false, length = 200)
    private Double finishLatitude;
    @Column(nullable = false, length = 200)
    private Double finishLongitude;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reservation", orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    @Builder
    public Reservation(String title, LocalDate reservationDate, LocalTime reservationTime, String startingPlace, String destination, Sex sex, Integer passengerNum, String challengeWord
                       , Integer currentNum, String countersignWord, Double startLatitude, Double startLongitude, Double finishLatitude, Double finishLongitude, ReservationStatus reservationStatus, User user) {
        this.title = title;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.startingPlace = startingPlace;
        this.destination = destination;
        this.sex = sex;
        this.currentNum = currentNum;
        this.passengerNum = passengerNum;
        this.challengeWord = challengeWord;
        this.countersignWord = countersignWord;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.finishLatitude = finishLatitude;
        this.finishLongitude = finishLongitude;
        this.reservationStatus = reservationStatus;
        this.user = user;
    }
//== 연관 관계 메서드==//

    public void addParticipation(Participation participation){
        participations.add(participation);
        participation.mappingReservation(this);

    }

    public void subParticipation(Participation participation){
        participations.remove(participation);
        participation.mappingReservation(this);
    }

    // 이름 변경
    public void changeTitle(String title) {
        this.title=title;
    }

    //인원 추가
    public void addCurrentNum(){this.currentNum++;}


    //인원 빼기
    public void subtractCurrentNum(){
        this.currentNum--;
    }

    //상태 변경
    public void changeReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    //게시글 참여 가능 상태 로직
    public ReservationStatus checkStatus(Integer currentNum, Integer passengerNum) {
        Double rate = Double.valueOf(currentNum * 100 / passengerNum);

        if (currentNum.equals(passengerNum)){
            return ReservationStatus.DEADLINE;
        }
        else if (rate <= 50) {
            return ReservationStatus.POSSIBLE;
        }
        return ReservationStatus.IMMINENT;
    }

    public void changeStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
