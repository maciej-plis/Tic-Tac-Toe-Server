package matthias.tictactoe.web.authentication.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.configuration.JWTConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    private static JWTConfig jwtConfig = JWTConfig.getConfig();

    public static Object generateJWTForUser(User user) {
        String accessToken = Jwts
                .builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .claim("authorities",
                        user.getRoles().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey().getBytes())
                .compact();

        return new Object() {
            public String name = user.getUsername();
            public String token = jwtConfig.getPrefix() + accessToken;
            public long expire = new Date().getTime() + jwtConfig.getExpirationTime();
        };
    }

}
