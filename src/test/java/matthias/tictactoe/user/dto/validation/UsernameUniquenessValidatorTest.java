package matthias.tictactoe.user.dto.validation;

import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.database.UserEntity;
import matthias.tictactoe.user.dto.UserDto;
import matthias.tictactoe.user.dto.validators.UsernameUniquenessValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UsernameUniquenessValidatorTest {

    private UsernameUniquenessValidator validator;

    @Mock
    private UserFacade userFacade;

    @BeforeEach
    public void init() {
        initMocks(this);
        validator = new UsernameUniquenessValidator(userFacade);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyName"})
    void isValid_forExistingUsername_returnsFalse(String name) {
        when(userFacade.isUsernameAvailable(name)).thenReturn(false);

        boolean isValid = validator.isValid(name, null);

        Assertions.assertFalse(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"anyName"})
    void isValid_forNotExistingUsername_returnsTrue(String name) {
        when(userFacade.isUsernameAvailable(name)).thenReturn(true);

        boolean isValid = validator.isValid(name, null);

        Assertions.assertTrue(isValid);
    }
}
