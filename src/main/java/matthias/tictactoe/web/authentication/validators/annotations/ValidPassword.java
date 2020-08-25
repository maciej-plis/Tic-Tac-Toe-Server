package matthias.tictactoe.web.authentication.validators.annotations;

import matthias.tictactoe.web.authentication.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "The password must be at least 8 characters long and must contain at least one lowercase letter, uppercase letter, digit and one special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
