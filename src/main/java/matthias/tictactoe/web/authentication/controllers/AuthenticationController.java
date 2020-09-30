package matthias.tictactoe.web.authentication.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserCredentials;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;
import matthias.tictactoe.web.authentication.services.UserService;
import matthias.tictactoe.web.authentication.utils.JWTUtils;
import matthias.tictactoe.web.authentication.utils.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${client.url}", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegistration userRegistration, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(422).body(fieldErrorsToMap(bindingResult.getFieldErrors()));
        }

        User user = UserMapper.mapToUserWithRoles(userRegistration, Role.USER);
        user.setPassword( passwordEncoder.encode(user.getPassword()) );
        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCredentials userCredentials, HttpServletResponse response) {

        User user = userService.findUserByUsername(userCredentials.getUsername());

        if(user == null || !passwordEncoder.matches(userCredentials.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        response.addCookie(JWTUtils.generateJWTCookieForUser(user));

        return ResponseEntity.ok().build();
    }

    private Map<String, String> fieldErrorsToMap(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

}
