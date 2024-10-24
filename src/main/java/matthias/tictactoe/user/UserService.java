package matthias.tictactoe.user;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.user.database.UserRepository;
import matthias.tictactoe.user.dto.CreateUserRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static matthias.tictactoe.user.User.Role.USER;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id)
            .map(userMapper::toDomain);
    }

    Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(userMapper::toDomain);
    }

    Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(userMapper::toDomain);
    }

    boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    long createUser(CreateUserRequest request) {
        final var user = buildUser(request);
        final var userEntity = userRepository.save(userMapper.toEntity(user));
        return userEntity.getId();
    }

    public long createGuestUser() {
        final var guestUser = buildGuestUser();
        final var userEntity = userRepository.save(userMapper.toEntity(guestUser));
        return userEntity.getId();
    }

    private User buildUser(CreateUserRequest request) {
        return new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            Set.of(USER)
        );
    }

    private GuestUser buildGuestUser() {
        return new GuestUser("Guest_" + RandomStringUtils.random(6, "0123456789")); // TODO
    }
}
