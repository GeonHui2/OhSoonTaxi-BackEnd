package TasamBackend.Tasambackend.controller;

import TasamBackend.Tasambackend.dto.AddParticipationDto;
import TasamBackend.Tasambackend.response.DefaultRes;
import TasamBackend.Tasambackend.response.StatusCode;
import TasamBackend.Tasambackend.service.ParticipationService;
import TasamBackend.Tasambackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ReservationService reservationService;
    private final ParticipationService participationService;

    //참여 추가
    @PostMapping("/add")
    public ResponseEntity addParticipation(@RequestBody AddParticipationDto addParticipationDto, @RequestParam(name = "userUid") String userUid) throws IOException {

        Long id = participationService.addParticipation(addParticipationDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
    }

    //참여 삭제
    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity deleteParticipation(@PathVariable("reservationId") Long reservationId, @RequestParam(name = "userUid") String userUid) throws IOException{

        Boolean aBoolean = participationService.deleteParticipation(reservationId, userUid);

        return aBoolean.equals(Boolean.TRUE) ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    //참여 조회 로직
    @GetMapping("/check/{reservationId}")
    public ResponseEntity checkParticipation(@PathVariable("reservationId") Long reservationId, @RequestParam(name = "userUid") String userUid) throws IOException {

        Integer id = participationService.checkParticipationCheck(reservationId, userUid);

        if (id.equals(1))
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "신청하기", "신청하기"), HttpStatus.OK);

        else if (id.equals(2))
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "암구호 조회", "암구호 조회"), HttpStatus.OK);

        else if (id.equals(3))
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 취소하기", "참여 취소하기"), HttpStatus.OK);

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, "마감", "마감"), HttpStatus.OK);
    }


}
