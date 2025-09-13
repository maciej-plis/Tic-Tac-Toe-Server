package matthias.tictactoe.user.dto.validators;

import matthias.tictactoe.user.dto.validators.annotations.FieldValuesMatch;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldValuesMatchValidator implements ConstraintValidator<FieldValuesMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldValuesMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        boolean isValid = Objects.equals(fieldValue, fieldMatchValue);

        if (!isValid) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldMatch)
                .addConstraintViolation();
        }

        return isValid;
    }
}
