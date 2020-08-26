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

class UsernameUniquenessValidatorTest {
    private UsernameUniquenessValidator validator;

    @Mock
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        validator = new UsernameUniquenessValidator(userService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyName"})
    void isValid_forExistingUsername_returnsFalse(String name) {
        when(userService.findUserByUsername(name)).thenReturn(new User());

        boolean isValid = validator.isValid(name, null);

        Assertions.assertFalse(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyName"})
    void isValid_forNotExistingUsername_returnsTrue(String name) {
        when(userService.findUserByUsername(name)).thenReturn(null);

        boolean isValid = validator.isValid(name, null);

        Assertions.assertTrue(isValid);
    }
}