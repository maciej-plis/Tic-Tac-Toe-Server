package matthias.tictactoe.user.dto.validation;

import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.UserDto;
import matthias.tictactoe.user.dto.validators.EmailUniquenessValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class EmailUniquenessValidatorTest {

    private EmailUniquenessValidator validator;

    @Mock
    private UserFacade userFacade;

    @BeforeEach
    public void init() {
        initMocks(this);
        validator = new EmailUniquenessValidator(userFacade);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyEmail@gmail.com"})
    void isValid_forExistingEmail_returnsFalse(String email) {
        when(userFacade.isEmailAvailable(email)).thenReturn(false);

        boolean isValid = validator.isValid(email, null);

        Assertions.assertFalse(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyEmail@gmail.com"})
    void isValid_forNotExistingEmail_returnsTrue(String email) {
        when(userFacade.isEmailAvailable(email)).thenReturn(true);

        boolean isValid = validator.isValid(email, null);

        Assertions.assertTrue(isValid);
    }
}
