package TasamBackend.Tasambackend.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassphraseResponseDto {
    private String challengeWord;
    private String countersignWord;
}
