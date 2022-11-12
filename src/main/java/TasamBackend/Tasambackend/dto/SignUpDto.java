package TasamBackend.Tasambackend.dto;

import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;

@Getter
public class SignUpDto {

    private String uid;
    private String password;
    private String name;
    private String phoneNum;
    private String schoolNum;
    private Sex sex;
}
