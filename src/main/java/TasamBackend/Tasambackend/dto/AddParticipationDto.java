package TasamBackend.Tasambackend.dto;

import lombok.Getter;

@Getter
public class AddParticipationDto {

    private Long reservationId;
    private Integer seatPosition;
}
