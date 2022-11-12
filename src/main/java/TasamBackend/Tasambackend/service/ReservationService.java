package TasamBackend.Tasambackend.service;

import TasamBackend.Tasambackend.dto.AddReservationDto;
import TasamBackend.Tasambackend.dto.UpdateReservationDto;
import TasamBackend.Tasambackend.dto.response.CurrentReservationInfoDto;
import TasamBackend.Tasambackend.dto.response.ListReservationDto;
import TasamBackend.Tasambackend.dto.response.PassphraseResponseDto;
import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.user.User;
import TasamBackend.Tasambackend.repository.participation.ParticipationRepository;
import TasamBackend.Tasambackend.repository.reservation.ReservationRepository;
import TasamBackend.Tasambackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static TasamBackend.Tasambackend.entity.ReservationStatus.POSSIBLE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    //게시글 추가
    @Transactional
    public Long addReservation(AddReservationDto addReservationDto, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();

        Long reservationId = reservationRepository.save(
                Reservation.builder()
                        .title(addReservationDto.getTitle())
                        .reservationDate(addReservationDto.getReserveDate())
                        .reservationTime(addReservationDto.getReserveTime())
                        .startingPlace(addReservationDto.getStarting())
                        .destination(addReservationDto.getDestination())
                        .sex(addReservationDto.getSex())
                        .reservationStatus(POSSIBLE)
                        .passengerNum(addReservationDto.getPassengerNum())
                        .currentNum(0)
                        .challengeWord(addReservationDto.getChallengeWord())
                        .countersignWord(addReservationDto.getCountersignWord())
                        .latitude(addReservationDto.getLatitude())
                        .longitude(addReservationDto.getLongitude())
                        .build()
        ).getId();

        return reservationId;
    }

    //게시글 제목 변경
    @Transactional
    public Long updateReservation(UpdateReservationDto updateReservationDto, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        Reservation currReservation = reservationRepository.findById(updateReservationDto.getId()).get();
        if (currReservation.getUser() != addUser)
            return null;

        currReservation.changeTitle(updateReservationDto.getTitle());

        return currReservation.getId();
    }

    //게시글 삭제
    @Transactional
    public Long deleteReservation(Long reservationId, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        if (reservationRepository.findById(reservationId).get().getUser() != addUser)
            return null;

        reservationRepository.deleteById(reservationId);

        return reservationId;
    }

    //게시글 상세 정보 조회
    @Transactional
    public CurrentReservationInfoDto getCurrentReservationInfo(Long reservationId, String userUid){
        CurrentReservationInfoDto currentReservationInfoDto = new CurrentReservationInfoDto();

        Reservation currentReservation =reservationRepository.findById(reservationId).get();
        currentReservationInfoDto.setReservation(currentReservation);

        return currentReservationInfoDto;
    }

    //날짜별 게시글 리스트 조회
    @Transactional
    public List<ListReservationDto> getAllReservationList(LocalDate date){
        List<ListReservationDto> reservationList = new ArrayList<>();
        List<Reservation> originsReservation = reservationRepository.findAllByReservationDate(date);

        for (Reservation r : originsReservation) {
            ListReservationDto listReservationDto = new ListReservationDto();
            listReservationDto.setReservationDate(r.getReservationDate());
            listReservationDto.setTitle(r.getTitle());
            listReservationDto.setSex(r.getSex());
            listReservationDto.setPassengerNum(r.getPassengerNum());
            listReservationDto.setStarting(r.getStartingPlace());
            listReservationDto.setDestination(r.getDestination());
            listReservationDto.setReservationStatus(r.getReservationStatus());

            reservationList.add(listReservationDto);
        }

        return reservationList;
    }

    //내가 생성한 게시글 리스트 조회
    @Transactional
    public List<ListReservationDto> getAllMyReservationList(String userUid){
        User currUser = userRepository.findByUid(userUid).get();
        List<ListReservationDto> reservationList = new ArrayList<>();
        List<Reservation> originsReservation =reservationRepository.findAllByUser(currUser);

        for (Reservation r : originsReservation) {
            ListReservationDto listReservationDto = new ListReservationDto();
            listReservationDto.setReservationDate(r.getReservationDate());
            listReservationDto.setTitle(r.getTitle());
            listReservationDto.setSex(r.getSex());
            listReservationDto.setPassengerNum(r.getPassengerNum());
            listReservationDto.setStarting(r.getStartingPlace());
            listReservationDto.setDestination(r.getDestination());
            listReservationDto.setReservationStatus(r.getReservationStatus());

            reservationList.add(listReservationDto);
        }

        return reservationList;
    }

    //해당 게시글 암구호 조회
    @Transactional
    public PassphraseResponseDto getPassphraseResponse(Long reservationId, String userUid)throws IOException{
        PassphraseResponseDto currPassphrase = new PassphraseResponseDto();
        Reservation currentReservation =reservationRepository.findById(reservationId).get();

        currPassphrase.setChallengeWord(currPassphrase.getChallengeWord());
        currPassphrase.setCountersignWord(currentReservation.getCountersignWord());

        return currPassphrase;
    }
}
