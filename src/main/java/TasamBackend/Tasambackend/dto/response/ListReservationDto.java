package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ListReservationDto {

    private Long id;
    private LocalTime reservationTime;
    private String title;
    private Sex sex;
    private String startingPlace;
    private String destination;
    private ReservationStatus reservationStatus;
}
