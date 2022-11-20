package TasamBackend.Tasambackend.service;

import TasamBackend.Tasambackend.dto.AddReservationDto;
import TasamBackend.Tasambackend.dto.UpdateReservationDto;
import TasamBackend.Tasambackend.dto.response.*;
import TasamBackend.Tasambackend.entity.Participation;
import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.ReservationStatus;
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
                        .startingPlace(addReservationDto.getStartingPlace())
                        .destination(addReservationDto.getDestination())
                        .sex(addReservationDto.getSex())
                        .currentNum(0)
                        .passengerNum(addReservationDto.getPassengerNum())
                        .challengeWord(addReservationDto.getChallengeWord())
                        .countersignWord(addReservationDto.getCountersignWord())
                        .startLatitude(addReservationDto.getStartLatitude())
                        .startLongitude(addReservationDto.getStartLongitude())
                        .finishLatitude(addReservationDto.getFinishLatitude())
                        .finishLongitude(addReservationDto.getFinishLongitude())
                        .reservationStatus(ReservationStatus.POSSIBLE)
                        .user(addUser)
                        .build()
        ).getId();

        return reservationId;
    }

    //게시글 제목 변경
    @Transactional
    public Long updateReservation(UpdateReservationDto updateReservationDto, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        Reservation currReservation = reservationRepository.findById(updateReservationDto.getReservationId()).get();
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
    public CurrentReservationInfoDto getCurrentReservationInfo(Long reservationId){
        User addUser = reservationRepository.findById(reservationId).get().getUser();
        CurrentReservationInfoDto currentReservationInfoDto = new CurrentReservationInfoDto();
        Reservation currentReservation =reservationRepository.findById(reservationId).get();
        List<ParticipationListDto> participationList = new ArrayList<>();

        for (Participation p : currentReservation.getParticipations()) {
            ParticipationListDto participationListDto = new ParticipationListDto();
            participationListDto.setName(p.getUser().getName());
            participationListDto.setSchoolNum(p.getUser().getSchoolNum());

            participationList.add(participationListDto);
        }

        currentReservationInfoDto.setTitle(currentReservation.getTitle());
        currentReservationInfoDto.setReserveDate(currentReservation.getReservationDate());
        currentReservationInfoDto.setReserveTime(currentReservation.getReservationTime());
        currentReservationInfoDto.setStartingPlace(currentReservation.getStartingPlace());
        currentReservationInfoDto.setDestination(currentReservation.getDestination());
        currentReservationInfoDto.setSex(currentReservation.getSex());
        currentReservationInfoDto.setCreatedAt(currentReservation.getCreatedAt());
        currentReservationInfoDto.setCurrentNum(participationList.size());
        currentReservationInfoDto.setPassengerNum(currentReservation.getPassengerNum());
        currentReservationInfoDto.setChallengeWord(currentReservation.getChallengeWord());
        currentReservationInfoDto.setCountersignWord(currentReservation.getCountersignWord());
        currentReservationInfoDto.setStartLatitude(currentReservation.getStartLatitude());
        currentReservationInfoDto.setStartLongitude(currentReservation.getStartLongitude());
        currentReservationInfoDto.setFinishLatitude(currentReservation.getFinishLatitude());
        currentReservationInfoDto.setFinishLongitude(currentReservation.getFinishLongitude());
        currentReservationInfoDto.setReservationStatus(currentReservation.getReservationStatus());
        currentReservationInfoDto.setUserUid(currentReservation.getUser().getUid());
        currentReservationInfoDto.setName(addUser.getName());
        currentReservationInfoDto.setSchoolNum(addUser.getSchoolNum());
        currentReservationInfoDto.setParticipations(participationList);

        return currentReservationInfoDto;
    }

    //날짜별 게시글 리스트 조회
    @Transactional
    public List<ListReservationDto> getAllReservationList(LocalDate reservationDate){
        List<ListReservationDto> reservationList = new ArrayList<>();
        List<Reservation> originsReservation = reservationRepository.findAllByReservationDate(reservationDate);

        for (Reservation r : originsReservation) {
            ListReservationDto listReservationDto = new ListReservationDto();
            listReservationDto.setId(r.getId());
            listReservationDto.setReservationTime(r.getReservationTime());
            listReservationDto.setTitle(r.getTitle());
            listReservationDto.setSex(r.getSex());
            listReservationDto.setStartingPlace(r.getStartingPlace());
            listReservationDto.setDestination(r.getDestination());
            listReservationDto.setReservationStatus(r.getReservationStatus());

            reservationList.add(listReservationDto);
        }

        return reservationList;
    }

    //내가 생성한 게시글 리스트 조회
    @Transactional
    public List<ListReservationDto> getAllMyReservationList(String userUid){
        User addUser = userRepository.findByUid(userUid).get();
        List<ListReservationDto> reservationList = new ArrayList<>();
        List<Reservation> originsReservation =reservationRepository.findAllByUser(addUser);

        for (Reservation r : originsReservation) {
            ListReservationDto listReservationDto = new ListReservationDto();
            listReservationDto.setId(r.getId());
            listReservationDto.setReservationTime(r.getReservationTime());
            listReservationDto.setTitle(r.getTitle());
            listReservationDto.setSex(r.getSex());
            listReservationDto.setStartingPlace(r.getStartingPlace());
            listReservationDto.setDestination(r.getDestination());
            listReservationDto.setReservationStatus(r.getReservationStatus());

            reservationList.add(listReservationDto);
        }

        return reservationList;
    }

    //내가 참여한 거 조회
    @Transactional
    public List<ListMyParticipationDto> getAllMyParticipationList(String userUid) {
        User addUser = userRepository.findByUid(userUid).get();
        List<ListMyParticipationDto> participationList = new ArrayList<>();
        List<Participation> participations = participationRepository.findAllByUser(addUser);
        List<Reservation> originsReservation = new ArrayList<>();
        for (Participation p : participations) {
            Reservation reservation = reservationRepository.findById(p.getReservation().getId()).get();
            originsReservation.add(reservation);
        }

        for (Reservation r : originsReservation) {
            ListMyParticipationDto listMyParticipationDto = new ListMyParticipationDto();
            listMyParticipationDto.setId(r.getId());
            listMyParticipationDto.setReservationTime(r.getReservationTime());
            listMyParticipationDto.setTitle(r.getTitle());
            listMyParticipationDto.setSex(r.getSex());
            listMyParticipationDto.setStartingPlace(r.getStartingPlace());
            listMyParticipationDto.setDestination(r.getDestination());
            listMyParticipationDto.setReservationStatus(r.getReservationStatus());

            participationList.add(listMyParticipationDto);
        }

        return participationList;
    }

    //해당 게시글 암구호 조회
    @Transactional
    public PassphraseResponseDto getPassphraseResponse(Long reservationId)throws IOException{
        PassphraseResponseDto currPassphrase = new PassphraseResponseDto();
        Reservation currentReservation =reservationRepository.findById(reservationId).get();

        currPassphrase.setChallengeWord(currentReservation.getChallengeWord());
        currPassphrase.setCountersignWord(currentReservation.getCountersignWord());

        return currPassphrase;
    }

    // 게시글 검색 - 게시글 리스트 조회
    @Transactional
    public List<ListSearchReservationDto> searchList(String keyword){
        List<Reservation> reservations = reservationRepository.findByTitleContaining(keyword);
        List<ListSearchReservationDto> list = new ArrayList<>();

        for (Reservation r : reservations) {
            ListSearchReservationDto listSearchReservationDto = new ListSearchReservationDto();
            listSearchReservationDto.setId(r.getId());
            listSearchReservationDto.setReservationDate(r.getReservationDate());
            listSearchReservationDto.setReservationTime(r.getReservationTime());
            listSearchReservationDto.setTitle(r.getTitle());
            listSearchReservationDto.setSex(r.getSex());
            listSearchReservationDto.setStartingPlace(r.getStartingPlace());
            listSearchReservationDto.setDestination(r.getDestination());
            listSearchReservationDto.setReservationStatus(r.getReservationStatus());

            list.add(listSearchReservationDto);
        }

        return list;
    }

}
