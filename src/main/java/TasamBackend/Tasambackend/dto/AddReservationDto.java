package TasamBackend.Tasambackend.dto;

import TasamBackend.Tasambackend.entity.ReservationStatus;
import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class AddReservationDto {

    private String title;
    private LocalDate reserveDate;
    private LocalTime reserveTime;
    private String starting;
    private String destination;
    private Sex sex;
    private ReservationStatus reservationStatus;
    private Integer passengerNum;
    private String challengeWord;
    private String countersignWord;
    private Double latitude;
    private Double longitude;
}
