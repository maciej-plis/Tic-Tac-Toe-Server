package matthias.tictactoe.web.authentication.helpers;

import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;

public class UserMapper {

    public static User mapToUser(UserRegistration userRegistration) {
        return User
            .builder()
            .username(userRegistration.getUsername())
            .password(userRegistration.getPassword())
            .email(userRegistration.getEmail())
            .build();
    }

}
