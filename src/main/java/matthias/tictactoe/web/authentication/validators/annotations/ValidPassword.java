package matthias.tictactoe.web.authentication.validators.annotations;

import matthias.tictactoe.web.authentication.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates annotated password field according to those rules:
 * <ul>
 *     <li>at least 8 characters</li>
 *     <li>at least 1 lowercase letter</li>
 *     <li>at least 1 uppercase letter</li>
 *     <li>at least 1 digit</li>
 *     <li>at least 1 special character (!@#$%&*_) excluding brackets</li>
 * </ul>
 * password cannot contain any other symbol
 */
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "The password must be at least 8 characters long and must contain at least one lowercase letter, uppercase letter, digit and one special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
