package matthias.tictactoe.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.time.Duration;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@ConfigurationProperties("jwt")
class JwtConfig {

    public JwtConfig(Duration expiration, String key) {
        this.expiration = expiration;
        this.secretKey = hmacShaKeyFor(key.getBytes(UTF_8));
    }

    private final Duration expiration;
    private final SecretKey secretKey;
}
