package matthias.tictactoe.user.dto.validators.annotations;

import matthias.tictactoe.user.dto.validators.UsernameUniquenessValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates uniqueness of annotated username field
 * by checking usernames of already registered users.
 */
@Constraint(validatedBy = UsernameUniquenessValidator.class)
@Target( { ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Username already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
