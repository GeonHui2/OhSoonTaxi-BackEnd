package TasamBackend.Tasambackend.service;

import TasamBackend.Tasambackend.dto.AddParticipationDto;
import TasamBackend.Tasambackend.entity.Participation;
import TasamBackend.Tasambackend.entity.Reservation;
import TasamBackend.Tasambackend.entity.Sex;
import TasamBackend.Tasambackend.entity.user.User;
import TasamBackend.Tasambackend.repository.participation.ParticipationRepository;
import TasamBackend.Tasambackend.repository.reservation.ReservationRepository;
import TasamBackend.Tasambackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static TasamBackend.Tasambackend.entity.ReservationStatus.DEADLINE;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ParticipationRepository participationRepository;

    //참여 추가
    @Transactional
    public Long addParticipation(AddParticipationDto addParticipationDto, String userUid) throws IOException {
        Reservation currReservation = reservationRepository.findById(addParticipationDto.getReservationId()).get();
        User addUser = userRepository.findByUid(userUid).get();

        if (currReservation.getSex().equals(Sex.MAN))
            if (addUser.getSex().equals(Sex.WOMAN))
                return null;

        else if (currReservation.getSex().equals(Sex.WOMAN))
            if (addUser.getSex().equals(Sex.MAN))
                return null;

        Participation addParticipation = participationRepository.save(
                Participation.builder()
                        .user(addUser)
                        .reservation(currReservation)
                        .seatPosition(addParticipationDto.getSeatPosition())
                        .build()
        );

        currReservation.addCurrentNum();
        currReservation.addParticipation(addParticipation);
        currReservation.changeReservationStatus();

        return addParticipation.getId();
    }

    //참여 취소
    @Transactional
    public Boolean deleteParticipation(Long reservationId, String userUid) throws IOException{
        User addUser = userRepository.findByUid(userUid).get();
        Reservation currReservation = reservationRepository.findById(reservationId).get();
        Participation currParticipation = participationRepository.findByUserAndReservation(addUser, currReservation).get();

        currParticipation.getReservation().subtractCurrentNum();
        currParticipation.getReservation().subParticipation(currParticipation);
        currReservation.changeReservationStatus();

        participationRepository.delete(currParticipation);

        return Boolean.TRUE;
    }

    //참여자 목록 조회
    @Transactional
    public List getParticipationByReservation(Long reservationId){
        List<HashMap<String, Object>> participations = new ArrayList<>();
        List<Participation> originparticipations = participationRepository.findAllByReservationId(reservationId);

        for (Participation p : originparticipations) {
            HashMap<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("seatPosition", p.getSeatPosition());
            map.put("schoolNum", p.getUser().getSchoolNum());
            map.put("name", p.getUser().getName());

            participations.add(map);
        }

        return participations;
    }

    //참여했는지 확인
    @Transactional
    public Boolean checkParticipation(Long reservationId, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        Reservation currReservation = reservationRepository.findById(reservationId).get();
        Boolean check = participationRepository.existsByUserAndReservation(addUser, currReservation);

        return check;
    }

    // 참여 조회 로직
    @Transactional
    public Integer checkParticipationCheck(Long reservationId, String userUid) throws IOException {
        Reservation currReservation = reservationRepository.findById(reservationId).get();
        Boolean check = checkParticipation(reservationId, userUid);
        LocalDateTime dateTime = LocalDateTime.of(currReservation.getReservationDate(), currReservation.getReservationTime());
        LocalDateTime passphraseDateTime = LocalDateTime.of(currReservation.getReservationDate(), currReservation.getReservationTime().minusMinutes(30));

        if (check.equals(Boolean.TRUE)) {
            if (LocalDateTime.now().isBefore(dateTime) && LocalDateTime.now().isAfter(passphraseDateTime))
                return 2;

            else if (LocalDateTime.now().isBefore(passphraseDateTime))
                return 3;

            return 4;
        }

        if (currReservation.getReservationStatus().equals(DEADLINE) || LocalDateTime.now().isAfter(dateTime))
            return 4;

        return 1;
    }
}
