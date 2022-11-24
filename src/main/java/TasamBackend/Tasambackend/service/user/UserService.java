package TasamBackend.Tasambackend.service.user;

import TasamBackend.Tasambackend.dto.SignInDto;
import TasamBackend.Tasambackend.dto.SignUpDto;
import TasamBackend.Tasambackend.dto.response.MyInfoDto;
import TasamBackend.Tasambackend.entity.user.RefreshToken;
import TasamBackend.Tasambackend.entity.user.User;
import TasamBackend.Tasambackend.filter.CustomAuthenticationEntryPoint;
import TasamBackend.Tasambackend.repository.participation.ParticipationRepository;
import TasamBackend.Tasambackend.repository.reservation.ReservationRepository;
import TasamBackend.Tasambackend.repository.user.RefreshTokenRepository;
import TasamBackend.Tasambackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    //회원가입
    @Transactional
    public Long signUp(SignUpDto user){
        Long id =userRepository.save(
                User.builder()
                        .uid(user.getUid())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .name(user.getName())
                        .phoneNum(user.getPhoneNum())
                        .schoolNum(user.getSchoolNum())
                        .sex(user.getSex())
                        .build())
                .getId();
        return id;
    }

    //로그인
    @Transactional
    public Boolean signIn (String refreshToken, User user) {
        refreshTokenRepository.save(new RefreshToken(refreshToken));

        logger.info(user.getUid() + " (id : " + user.getId() + ") login");
        return true;
    }

    //로그아웃
    @Transactional
    public Boolean signOut(String refreshToken, User user) {
        if (!refreshTokenRepository.existsByRefreshToken(refreshToken))
            return false;

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        logger.info(user.getUid() + " (id : " + user.getId() + ") logout");
        return true;
    }


    //아이디 중복
    @Transactional
    public Boolean checkUnique(String uid) {
        Boolean result = userRepository.existsByUid(uid);
        return !result;
    }

    //로그인 확인
    @Transactional
    public Optional<User> findUserByUidAndPassword(String uid, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Optional<User> member= userRepository.findByUidAndAndPassword(uid, encodedPassword);

        return member;
    }

    //유저 확인
    @Transactional
    public Optional<User> findUserByUid(String uid) {
        Optional<User> member = userRepository.findByUid(uid);
        return member;
    }

    //비밀번호 확인
    @Transactional
    public boolean checkPassword(SignInDto user, User member) {
        return passwordEncoder.matches(user.getPassword(), member.getPassword());
    }

    //내 정보 조회
    @Transactional
    public MyInfoDto myInfoDto(String uid) {
        User member = userRepository.findByUid(uid).get();
        MyInfoDto myInfoDto = new MyInfoDto();

        myInfoDto.setName(member.getName());
        myInfoDto.setSex(member.getSex());
        myInfoDto.setSchoolNum(member.getSchoolNum());

        return myInfoDto;
    }
}
