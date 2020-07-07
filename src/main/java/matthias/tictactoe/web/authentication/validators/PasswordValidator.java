package matthias.tictactoe.web.authentication.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements
        ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext ctx) {
        return password != null
            && password.matches("^(?=.*[a-z]).*$") // lowercase letter
            && password.matches("^(?=.*[A-Z]).*$") // uppercase letter
            && password.matches("^(?=.*\\d).*$") // digit
            && password.matches("^(?=.*[!@#$%&*_]).*$") // special character
            && password.matches("^.{8,}$") // at least 8 characters
            && password.matches("[a-zA-Z\\d!@#$%&*_]*"); //only legal characters

            // regex combined ^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[a-zA-Z\d@$!%*?&]{8,}$
    }
}
