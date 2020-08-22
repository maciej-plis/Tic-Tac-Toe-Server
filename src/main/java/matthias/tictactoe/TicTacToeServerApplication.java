package matthias.tictactoe;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication
public class TicTacToeServerApplication implements CommandLineRunner {
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        User user1 = User.builder()
                        .username("RICO5k")
                        .email("maciekqwertyzaqwsx@gmail.com")
                        .password("$2a$10$q5pIy.6HnZyj8bmk6gUr8umQodqsd1PUaJvg4IcEQa.BMRlyZV196")
                        .roles(roles)
                        .build();
        userService.saveUser(user1);

        User user2 = User.builder()
                .username("alfa1")
                .email("alfa1@gmail.com")
                .password("$2a$10$TI/v1CU.RCswJoQJMbLD5usP6aSoNigYJWRS01RNxSGyDDIEKo/wm")
                .roles(roles)
                .build();
        userService.saveUser(user2);
    }
}

