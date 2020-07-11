package matthias.tictactoe.web.authentication.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.repos.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword())); // TODO remove side effect
        user.setRoles(new HashSet<>(Arrays.asList(Role.USER))); // TODO remove side effect

        return userRepository.save(user);
    }
}
