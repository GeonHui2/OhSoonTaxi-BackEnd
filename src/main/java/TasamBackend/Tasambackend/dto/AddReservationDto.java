package TasamBackend.Tasambackend.dto;

import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class AddReservationDto {

    private String title;
    private LocalDate reserveDate;
    private LocalTime reserveTime;
    private String startingPlace;
    private String destination;
    private Sex sex;
    private LocalTime createdAt;
    private Integer passengerNum;
    private String challengeWord;
    private String countersignWord;
    private Double startLatitude;
    private Double startLongitude;
    private Double finishLatitude;
    private Double finishLongitude;
}
