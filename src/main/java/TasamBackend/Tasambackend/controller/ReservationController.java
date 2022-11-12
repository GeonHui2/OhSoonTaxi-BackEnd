package TasamBackend.Tasambackend.controller;

import TasamBackend.Tasambackend.dto.AddReservationDto;
import TasamBackend.Tasambackend.dto.response.CurrentReservationInfoDto;
import TasamBackend.Tasambackend.dto.response.PassphraseResponseDto;
import TasamBackend.Tasambackend.response.DefaultRes;
import TasamBackend.Tasambackend.response.StatusCode;
import TasamBackend.Tasambackend.service.ReservationService;
import TasamBackend.Tasambackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    //게시글 추가
    @PostMapping("/add")
    public ResponseEntity addReservation(@RequestAttribute("userUid") String userUid, @RequestBody AddReservationDto addReservationDto) throws IOException {

        Long id = reservationService.addReservation(addReservationDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "택시 예약 생성 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
    }

    //게시글 삭제
    @GetMapping("/delete")
    public ResponseEntity deleteReservation(@RequestAttribute("userUid") String userUid, @RequestBody HashMap<String, Long> param) throws IOException{
        Long id = reservationService.deleteReservation(param.get("reservationId"), userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    //게시글 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity getReservation(@RequestAttribute("userUid") String userUid, @PathVariable("reservationId") Long reservationId) throws IOException{
        CurrentReservationInfoDto currentReservationInfoDto = reservationService.getCurrentReservationInfo(reservationId, userUid);

        return currentReservationInfoDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 게시글 조회 완료", currentReservationInfoDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 없음", currentReservationInfoDto), HttpStatus.OK);

    }

    //날짜별 게시글 리스트 조회
    @GetMapping("/list/{reservationDate}")
    public ResponseEntity getReservationList(@RequestAttribute("userUid") String userUid, @PathVariable("reservationDate") LocalDate reservationDate) throws IOException{
        List reservations =reservationService.getAllReservationList(reservationDate);

        return reservations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 리스트 조회 완료", reservations), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 리스트 없음", new ArrayList()), HttpStatus.OK);
    }

    //게시글의 암구호 조회
    @GetMapping("/passphrase/{reservationId}")
    public ResponseEntity getPassphrase(@RequestAttribute("userUid") String userUid, @PathVariable("reservationId") Long reservationId) throws IOException{
        PassphraseResponseDto passphraseResponseDto = reservationService.getPassphraseResponse(reservationId, userUid);

        return passphraseResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "암구호 조회 완료", passphraseResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 암구호 없음", passphraseResponseDto), HttpStatus.BAD_REQUEST);
    }

    //마이페이지 내가 만든 게시글 조회
    @GetMapping("list/Reservations")
    public ResponseEntity getMyReservations(@RequestAttribute("userUid") String userUid) throws IOException {
        List myReservations = reservationService.getAllMyReservationList(userUid);

        return myReservations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 조회 완료", myReservations), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 없음", new ArrayList()), HttpStatus.BAD_REQUEST);
    }
}
