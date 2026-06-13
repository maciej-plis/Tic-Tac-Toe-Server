package matthias.tictactoe.user.dto.validators.annotations;

import matthias.tictactoe.user.dto.validators.FieldValuesMatchValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates sameness of 2 specified fields by comparing them.
 * Constrain violation is applied to <b>fieldMatch</b>.
 */
@Constraint(validatedBy = FieldValuesMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValuesMatch {

    String message() default "Field values don't match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    String fieldMatch();
}
