package TasamBackend.Tasambackend.service.user;

import TasamBackend.Tasambackend.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback(value = false)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("비밀번호 테스트")
    void password() {
        String password = "1234";
        String endcordPassword1 = passwordEncoder.encode(password);
        String endcordPassword2 = passwordEncoder.encode(password);

        System.out.println("endcordPassword1 = " + endcordPassword1);
        System.out.println("endcordPassword2 = " + endcordPassword2);

        String truePassword = "1234";
        String falsePassword = "0000";

        System.out.println("truePassword endcord1 = " + passwordEncoder.matches(truePassword, endcordPassword1));
        System.out.println("falsePassword endcord1 = " + passwordEncoder.matches(falsePassword, endcordPassword1));
        System.out.println("truePassword endcord2 = " + passwordEncoder.matches(truePassword, endcordPassword2));
        System.out.println("falsePassword endcord2 = " + passwordEncoder.matches(falsePassword, endcordPassword2));
    }

    @Test
    @DisplayName("아이디 체크 테스트")
    void check() {
        Boolean result = userRepository.existsByUid("chanu");
        System.out.println(result);

    }
}