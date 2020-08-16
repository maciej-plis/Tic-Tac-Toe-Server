package matthias.tictactoe.web.authentication.validators;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class UserRegistrationValidator {

    private final UserService userService;

    public void isUsernameAlreadyRegistered(String username, BindingResult bindingResult) {
        boolean usernameExists = userService.findUserByUsername(username)  != null;
        if(usernameExists) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");
        }
    }

    public void isEmailAlreadyRegistered(String email, BindingResult bindingResult) {
        boolean emailExists = userService.findUserByEmail(email) != null;
        if(emailExists) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
    }

}
