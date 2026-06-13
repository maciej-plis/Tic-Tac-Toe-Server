package matthias.tictactoe.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.Data;

import java.util.Date;

@Data
public class ParsedTokenDto {

    private final Claims claims;

    public String getSubject() {
        return claims.getSubject();
    }

    public boolean isExpired() {
        return claims.getExpiration().after(new Date());
    }
}
