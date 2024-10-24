package matthias.tictactoe.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import matthias.tictactoe.jwt.dto.ParsedTokenDto;
import matthias.tictactoe.jwt.dto.TokenDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    public TokenDto generateJWT(String subject) {
        long issuedAt = System.currentTimeMillis();
        long expiration = issuedAt + jwtConfig.getExpiration().toMillis();

        String accessToken = Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(subject)
            .issuedAt(new Date(issuedAt))
            .expiration(new Date(expiration))
            .signWith(jwtConfig.getSecretKey())
            .compact();

        return TokenDto.builder()
            .token_type("Bearer")
            .access_token(accessToken)
            .expires_in(expiration)
            .build();
    }

    public ParsedTokenDto parseToken(String token) {
        return new ParsedTokenDto(Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload());
    }
}
