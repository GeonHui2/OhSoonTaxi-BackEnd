package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class CurrentReservationInfoDto {

    private Reservation reservation;
}
