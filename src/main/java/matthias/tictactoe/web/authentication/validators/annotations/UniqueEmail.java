package matthias.tictactoe.web.authentication.validators.annotations;

import matthias.tictactoe.web.authentication.validators.EmailUniquenessValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailUniquenessValidator.class)
@Target( { ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
