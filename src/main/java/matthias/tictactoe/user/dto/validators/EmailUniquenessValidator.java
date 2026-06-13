package matthias.tictactoe.user.dto.validators;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.validators.annotations.UniqueEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailUniquenessValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserFacade userFacade;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        return userFacade.isEmailAvailable(email);
    }
}
