package matthias.tictactoe.web.authentication.validators;

import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

class EmailUniquenessValidatorTest {
    private EmailUniquenessValidator validator;

    @Mock
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        validator = new EmailUniquenessValidator(userService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyEmail@gmail.com"})
    void isValid_forExistingEmail_returnsFalse(String email) {
        when(userService.findUserByEmail(email)).thenReturn(new User());

        boolean isValid = validator.isValid(email, null);

        Assertions.assertFalse(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyEmail@gmail.com"})
    void isValid_forNotExistingEmail_returnsTrue(String email) {
        when(userService.findUserByEmail(email)).thenReturn(null);

        boolean isValid = validator.isValid(email, null);

        Assertions.assertTrue(isValid);
    }
}