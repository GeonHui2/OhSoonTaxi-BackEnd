package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ListSearchReservationDto {

    private Long id;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String title;
    private Sex sex;
    private String startingPlace;
    private String destination;
    private ReservationStatus reservationStatus;
}
