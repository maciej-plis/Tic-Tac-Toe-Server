package matthias.tictactoe.web.authentication.validators;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.services.UserService;
import matthias.tictactoe.web.authentication.validators.annotations.UniqueUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameUniquenessValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext ctx) {
        return userService.findUserByUsername(username) == null;
    }
}
