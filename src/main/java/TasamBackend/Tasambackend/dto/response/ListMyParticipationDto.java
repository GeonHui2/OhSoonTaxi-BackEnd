package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ListMyParticipationDto {

    private LocalDate reservationDate;
    private String title;
    private Sex sex;
    private Integer passengerNum;
    private String starting;
    private String destination;
    private ReservationStatus reservationStatus;
}

