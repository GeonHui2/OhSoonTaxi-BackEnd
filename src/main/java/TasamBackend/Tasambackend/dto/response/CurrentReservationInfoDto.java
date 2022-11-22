package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class CurrentReservationInfoDto {

    private String title;
    private LocalDate reserveDate;
    private LocalTime reserveTime;
    private String startingPlace;
    private String destination;
    private Sex sex;
    private LocalDateTime createdAt;
    private Integer currentNum;
    private Integer passengerNum;
    private String challengeWord;
    private String countersignWord;
    private Double startLatitude;
    private Double startLongitude;
    private Double finishLatitude;
    private Double finishLongitude;
    private ReservationStatus reservationStatus;
    private String userUid;
    private String name;
    private String schoolNum;
    private Sex userSex;
    private List<ParticipationListDto> participations;
}
