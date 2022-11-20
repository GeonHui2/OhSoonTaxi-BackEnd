package TasamBackend.Tasambackend.controller;

import TasamBackend.Tasambackend.dto.AddReservationDto;
import TasamBackend.Tasambackend.dto.UpdateReservationDto;
import TasamBackend.Tasambackend.dto.response.CurrentReservationInfoDto;
import TasamBackend.Tasambackend.dto.response.PassphraseResponseDto;
import TasamBackend.Tasambackend.response.DefaultRes;
import TasamBackend.Tasambackend.response.StatusCode;
import TasamBackend.Tasambackend.service.ReservationService;
import TasamBackend.Tasambackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    //게시글 추가
    @PostMapping("/add")
    public ResponseEntity addReservation(@RequestBody AddReservationDto addReservationDto, @RequestParam(name = "userUid") String userUid) throws IOException {

        Long id = reservationService.addReservation(addReservationDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "택시 예약 생성 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity deleteReservation(@PathVariable("reservationId") Long reservationId, @RequestParam(name = "userUid") String userUid) throws IOException{
        Long id = reservationService.deleteReservation(reservationId, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    //게시글 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity getReservation(@PathVariable(name = "reservationId") Long reservationId) throws IOException{
        CurrentReservationInfoDto currentReservationInfoDto = reservationService.getCurrentReservationInfo(reservationId);

        return currentReservationInfoDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 게시글 조회 완료", currentReservationInfoDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 없음", currentReservationInfoDto), HttpStatus.OK);

    }

    //날짜별 게시글 리스트 조회
    @GetMapping("/list")
    public ResponseEntity getReservationList(@RequestParam(name = "reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate) throws IOException{
        List reservations = reservationService.getAllReservationList(reservationDate);

        return reservations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 리스트 조회 완료", reservations), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 리스트 없음", new ArrayList()), HttpStatus.OK);
    }

    //게시글의 암구호 조회
    @GetMapping("/passphrase/{reservationId}")
    public ResponseEntity getPassphrase(@PathVariable(name = "reservationId") Long reservationId, @RequestParam(name = "userUid") String userUid) throws IOException{
        PassphraseResponseDto passphraseResponseDto = reservationService.getPassphraseResponse(reservationId);

        return passphraseResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "암구호 조회 완료", passphraseResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 암구호 없음", passphraseResponseDto), HttpStatus.BAD_REQUEST);
    }

    //마이페이지 내가 만든 게시글 조회
    @GetMapping("/list/reservations")
    public ResponseEntity getMyReservations(@RequestParam(name = "userUid") String userUid) throws IOException {
        List myReservations = reservationService.getAllMyReservationList(userUid);

        return myReservations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 조회 완료", myReservations), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 게시글 없음", new ArrayList()), HttpStatus.BAD_REQUEST);
    }

    //내가 참여한 약속 조회
    @GetMapping("/list/participations")
    public ResponseEntity getMyParticipations(@RequestParam(name = "userUid") String userUid) throws IOException {
        List myParticipations = reservationService.getAllMyParticipationList(userUid);

        return myParticipations.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "참여 조회 완료", myParticipations), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.OK, " 조회된 참여 없음", new ArrayList()), HttpStatus.OK);
    }

    //게시글 제목 수정
    @PostMapping("/edit")
    public ResponseEntity updateReservation(@RequestBody UpdateReservationDto updateReservationDto, @RequestParam(name = "userUid") String userUid) throws IOException{
        Long id = reservationService.updateReservation(updateReservationDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "게시글 제목 수정 완료"), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    //게시글 검색 - 게시글 리스트 조회
    @GetMapping("/search/list")
    public ResponseEntity searchList(@RequestParam(value = "keyword") String keyword) throws IOException{
        List searchList = reservationService.searchList(keyword);

        return searchList.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "검색 완료", searchList), HttpStatus.OK) :
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 검색 결과 없음", new ArrayList()), HttpStatus.OK);
    }
}
