package matthias.tictactoe.web.authentication.validators;

import matthias.tictactoe.web.authentication.validators.annotations.FieldsValueMatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FieldsValueMatchValidatorTest {
    private FieldsValueMatchValidator validator;

    @Mock
    private ConstraintValidatorContext ctx;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder ctxBuilder;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeCtxBuilder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(ctx.buildConstraintViolationWithTemplate(any())).thenReturn(ctxBuilder);
        when(ctxBuilder.addPropertyNode(any())).thenReturn(nodeCtxBuilder);

        this.validator = new FieldsValueMatchValidator();
        this.validator.initialize(createAnnotation());
    }

    @ParameterizedTest
    @CsvSource({"whatever,whatever", ",", "a,a", "123 _!+,123 _!+"})
    void isValid_forMatchingStrings_returnsTrue(String value, String matchingValue) {
        Object annotatedBean = createDummyBean(value, matchingValue);

        boolean isValid = validator.isValid(annotatedBean, ctx);

        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @CsvSource({"nope,whatever", "ab,a", "123 ss_!+,123 _!+a"})
    void isValid_forDifferentStrings_returnsFalse(String value, String matchingValue) {
        Object annotatedBean = createDummyBean(value, matchingValue);

        boolean isValid = validator.isValid(annotatedBean, ctx);

        Assertions.assertFalse(isValid);
    }

    private Object createDummyBean(Object value, Object matchingValue) {
        return new Object() {
            public Object getValue() {
                return value;
            }
            public Object getMatchingValue() {
                return matchingValue;
            }
        };
    }

    private FieldsValueMatch createAnnotation() {
        return new FieldsValueMatch() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String field() {
                return "value";
            }

            @Override
            public String fieldMatch() {
                return "matchingValue";
            }
        };
    }
}