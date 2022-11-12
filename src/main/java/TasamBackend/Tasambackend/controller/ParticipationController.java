package TasamBackend.Tasambackend.controller;

import TasamBackend.Tasambackend.dto.AddParticipationDto;
import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.response.DefaultRes;
import TasamBackend.Tasambackend.response.StatusCode;
import TasamBackend.Tasambackend.service.ParticipationService;
import TasamBackend.Tasambackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static TasamBackend.Tasambackend.entity.ReservationStatus.DEADLINE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ReservationService reservationService;
    private final ParticipationService participationService;

    //참여 추가
    @PostMapping("/add")
    public ResponseEntity addParticipation(@RequestAttribute("userUid") String userUid, @RequestBody AddParticipationDto addParticipationDto) throws IOException {

        Long id = participationService.addParticipation(addParticipationDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
    }

    //참여 삭제
    @DeleteMapping("/delete")
    public ResponseEntity deleteParticipation(@RequestAttribute("userUid") String userUid, @RequestBody HashMap<String, Long> param) throws IOException{

        Long id = participationService.deleteParticipation(param.get("participationId"), userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    //참여 조회 로직
    @GetMapping("/check/{reservationId}")
    public ResponseEntity checkParticipation(@RequestAttribute("userUid") String userUid, @PathVariable("reservationId") Long reservationId) throws IOException {

        Long id = participationService.checkParticipation(reservationId, userUid);
        Reservation reservation = reservationService.getCurrentReservationInfo(reservationId, userUid).getReservation();

        if (LocalDate.now().equals(reservation.getReservationDate()) && LocalTime.now().isBefore(reservation.getReservationTime()) && reservation.getReservationStatus() != DEADLINE){
            if (id == null)
                return new ResponseEntity(DefaultRes.res(StatusCode.OK, "신청하기", "신청하기"), HttpStatus.OK);

            else if (LocalTime.now().minusMinutes(30).isBefore(reservation.getReservationTime()))
                return new ResponseEntity(DefaultRes.res(StatusCode.OK, "암구호", "암구호"), HttpStatus.OK);

            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "취소하기", "취소하기"), HttpStatus.OK);
        }

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, "마감", "마감"), HttpStatus.OK);
    }

    //내가 참여한 약속 조회
    @GetMapping("/list/Participations")
    public ResponseEntity getMyParticipations(@RequestAttribute("userUid") String userUid) throws IOException {
        List myParticipations = participationService.getAllMyParticipationList(userUid);

        return myParticipations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 조회 완료", myParticipations), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.OK, " 조회된 참여 없음", new ArrayList()), HttpStatus.OK);
    }
}
