package matthias.tictactoe.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.UserDto;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@NullMarked
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticatedUserProvider {

    private final UserFacade userFacade;

    public UserDto getAuthenticatedUser() {
        return ofNullable(getContext().getAuthentication())
            .map(Authentication::getName)
            .map(UUID::fromString)
            .map(userFacade::getUserOrThrow)
            .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
    }
}
