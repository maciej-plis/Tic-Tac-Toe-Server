package matthias.tictactoe.web.authentication.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordValidatorTest {
    private PasswordValidator validator;

    @BeforeEach
    public void init() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Whatever!123", "##alfa0Beta##"})
    void isValid_forValidPassword_returnsTrue(String password) {
        boolean isValid = validator.isValid(password, null);

        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "alfa", "alfabeta", "AlfaBeta", "AlfaBeta123", "AlfaBeta123!@;;", "!@#$%&*_!@#$%&*_", "12312799123"})
    void isValid_forInvalidPassword_returnsFalse(String password) {
        boolean isValid = validator.isValid(password, null);

        Assertions.assertFalse(isValid);
    }
}