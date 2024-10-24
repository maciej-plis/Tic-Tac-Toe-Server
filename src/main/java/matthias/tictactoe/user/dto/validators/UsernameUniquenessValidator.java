package matthias.tictactoe.user.dto.validators;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.validators.annotations.UniqueUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameUniquenessValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserFacade userFacade;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext ctx) {
        return userFacade.isUsernameAvailable(username);
    }
}
