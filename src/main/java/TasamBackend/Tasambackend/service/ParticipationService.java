package TasamBackend.Tasambackend.service;

import TasamBackend.Tasambackend.dto.AddParticipationDto;
import TasamBackend.Tasambackend.dto.response.ListMyParticipationDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationService {

    UserRepository userRepository;
    ReservationRepository reservationRepository;
    ParticipationRepository participationRepository;

    //참여 추가
    @Transactional
    public Long addParticipation(AddParticipationDto addParticipationDto, String userUid) throws IOException {
        Reservation currReservation = reservationRepository.findById(addParticipationDto.getReservationId()).get();
        User currParticipationUser = userRepository.findByUid(userUid).get();

        if (currReservation.getReservationStatus() == ReservationStatus.DEADLINE)
            return null;

        Participation participation = participationRepository.save(
                Participation.builder()
                        .user(currParticipationUser)
                        .reservation(currReservation)
                        .seatPosition(addParticipationDto.getSeatPosition())
                        .build()
        );

        currReservation.addCurrentNum();
        currReservation.addParticipation(participation);

        return participation.getId();
    }

    //참여 취소
    @Transactional
    public Long deleteParticipation(Long reservationId, String userUid) throws IOException{
        User addUser = userRepository.findByUid(userUid).get();
        Participation currParticipation = participationRepository.findById(reservationId).get();

        if (currParticipation.getUser() != addUser)
            return null;

        currParticipation.getReservation().subtractCurrentNum();
        currParticipation.getReservation().subParticipation(currParticipation);

        participationRepository.deleteById(currParticipation.getId());

        return currParticipation.getId();
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
    public Long checkParticipation(Long reservationId, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        Reservation currReservation = reservationRepository.findById(reservationId).get();
        Participation currParticipation = participationRepository.findByUserAndReservation(addUser, currReservation).get();

        if (currParticipation.getUser() != addUser)
            return null;

        return currParticipation.getId();
    }

    //내가 참여한 거 조회
    @Transactional
    public List<ListMyParticipationDto> getAllMyParticipationList(String userUid) {
        User addUser = userRepository.findByUid(userUid).get();
        List<ListMyParticipationDto> participationList = new ArrayList<>();
        List<Participation> originParticipations = participationRepository.findAllByUser(addUser);

        for (Participation p : originParticipations) {
            ListMyParticipationDto listMyParticipationDto = new ListMyParticipationDto();
        }

        return null;
    }
}
