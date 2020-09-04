package matthias.tictactoe.web.authentication.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
