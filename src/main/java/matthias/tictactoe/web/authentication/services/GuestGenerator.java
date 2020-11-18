package matthias.tictactoe.web.authentication.services;

import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class GuestGenerator {
    public User generateGuestUser() {
        String username = "Guest_" + RandomStringUtils.random(6, "0123456789");

        return User.builder()
                .username(username)
                .roles(new HashSet<Role>(Arrays.asList(Role.USER)))
                .build();
    }
}
