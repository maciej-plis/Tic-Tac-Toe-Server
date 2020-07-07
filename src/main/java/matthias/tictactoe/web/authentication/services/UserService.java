package matthias.tictactoe.web.authentication.services;

import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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
