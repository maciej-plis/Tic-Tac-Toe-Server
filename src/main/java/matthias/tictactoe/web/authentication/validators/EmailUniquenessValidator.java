package matthias.tictactoe.web.authentication.validators;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.services.UserService;
import matthias.tictactoe.web.authentication.validators.annotations.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailUniquenessValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        return userService.findUserByEmail(email) == null;
    }
}
