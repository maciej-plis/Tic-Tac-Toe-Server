package matthias.tictactoe.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matthias.tictactoe.user.dto.CreateUserRequest;
import matthias.tictactoe.user.dto.UserDto;
import matthias.tictactoe.user.exception.UserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public Optional<UserDto> findUserById(UUID id) {
        final var user = userService.findUserById(id);
        return user.map(userMapper::toDto);
    }

    public UserDto getUserOrThrow(UUID id) {
        return findUserById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public Optional<UserDto> findUserByUsername(String username) {
        final var user = userService.findUserByUsername(username);
        return user.map(userMapper::toDto);
    }

    public boolean isUsernameAvailable(String username) {
        return userService.isUsernameAvailable(username);
    }

    public Optional<UserDto> findUserByEmail(String email) {
        final var user = userService.findUserByEmail(email);
        return user.map(userMapper::toDto);
    }

    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }

    public UUID createGuestUser() {
        return userService.createGuestUser();
    }

    public UUID createUser(@Valid CreateUserRequest request) {
        return userService.createUser(request);
    }
}
