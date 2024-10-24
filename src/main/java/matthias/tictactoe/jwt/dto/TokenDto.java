package matthias.tictactoe.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String token_type;
    private String access_token;
    private long expires_in;
}
