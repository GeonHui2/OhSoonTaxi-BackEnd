package TasamBackend.Tasambackend.dto.response;

import TasamBackend.Tasambackend.entity.Sex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyInfoDto {

    private String name;
    private Sex sex;
    private String schoolNum;
}
