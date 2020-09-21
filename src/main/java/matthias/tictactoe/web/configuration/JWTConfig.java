package matthias.tictactoe.web.configuration;

import lombok.Getter;

@Getter
public class JWTConfig {
    private final String header = "Authorization";
    private final String prefix = "Bearer ";
    private final String secretKey = "aWyx1Y5bxQ83MM3QHDx5";
    private final int expirationTime = 86400000;

    private static JWTConfig config;

    private JWTConfig() {}

    public static JWTConfig getConfig() {
        if(config == null) {
            config = new JWTConfig();
        }

        return config;
    }

}