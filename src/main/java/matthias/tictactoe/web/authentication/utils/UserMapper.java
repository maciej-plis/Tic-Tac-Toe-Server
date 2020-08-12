package matthias.tictactoe.web.authentication.utils;

import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;

import java.util.Arrays;
import java.util.HashSet;

public class UserMapper {

    public static User mapToUserWithRoles(UserRegistration userRegistration, Role... roles) {
        return User
            .builder()
            .username(userRegistration.getUsername())
            .password(userRegistration.getPassword())
            .email(userRegistration.getEmail())
            .roles( new HashSet<>(Arrays.asList(roles) ))
            .build();
    }

}
