package TasamBackend.Tasambackend.entity.user;

import TasamBackend.Tasambackend.entity.Sex;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class UserWaiting {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false, length =20, unique = true)
    private String uid;
    @Column(nullable = false, length =200)
    private String password;
    @Column(nullable = false, length =50)
    private String name;
    @Column(nullable = false, length =30)
    private String phoneNum;
    @Column(nullable = false, length =8)
    private String schoolNum;

    @Enumerated(STRING)
    @Column(nullable = false, length =10)
    private Sex sex;

    @Builder
    public UserWaiting(String uid, String password, String name, String phoneNum, String schoolNum, Sex sex) {
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.schoolNum = schoolNum;
        this.sex = sex;
    }
}
