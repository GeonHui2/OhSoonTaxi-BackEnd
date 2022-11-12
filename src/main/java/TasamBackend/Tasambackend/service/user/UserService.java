package TasamBackend.Tasambackend.service.user;

import TasamBackend.Tasambackend.dto.SignInDto;
import TasamBackend.Tasambackend.dto.SignUpDto;
import TasamBackend.Tasambackend.entity.user.RefreshToken;
import TasamBackend.Tasambackend.entity.user.User;
import TasamBackend.Tasambackend.filter.CustomAuthenticationEntryPoint;
import TasamBackend.Tasambackend.repository.participation.ParticipationRepository;
import TasamBackend.Tasambackend.repository.reservation.ReservationRepository;
import TasamBackend.Tasambackend.repository.user.RefreshTokenRepository;
import TasamBackend.Tasambackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long signUp(SignUpDto signUpDto){
        Long id =userRepository.save(
                User.builder()
                        .uid(signUpDto.getUid())
                        .password(signUpDto.getPassword())
                        .name(signUpDto.getName())
                        .phoneNum(signUpDto.getPhoneNum())
                        .schoolNum(signUpDto.getSchoolNum())
                        .sex(signUpDto.getSex())
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
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        logger.info(user.getUid() + " (id : " + user.getId() + ") logout");
        return true;
    }


    //아이디 중복
    @Transactional
    public Boolean checkUnique(String uid) {
        Boolean result = userRepository.existsByUid(uid);

        return result;
    }

    //아이디 확인
    @Transactional
    public User findUserByUid(String user) {
        User member = userRepository.findByUid(user).get();

        return member;
    }

    //비밀번호 확인
    @Transactional
    public boolean checkPassword(User member, SignInDto user) {
        return passwordEncoder.matches(user.getPassword(), member.getPassword());
    }
}
