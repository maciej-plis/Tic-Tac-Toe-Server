package matthias.tictactoe.web.authentication.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.helpers.UserMapper;
import matthias.tictactoe.web.authentication.helpers.UserRegistrationValidator;
import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;
import matthias.tictactoe.web.authentication.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRegistrationValidator registrationValidator;
    private final UserService userService;

    @RequestMapping("/register")
    public ResponseEntity<Object> createNewUser(@Valid UserRegistration userRegistration, BindingResult bindingResult) {

        registrationValidator.isUsernameAlreadyRegistered(userRegistration.getUsername(), bindingResult);
        registrationValidator.isEmailAlreadyRegistered(userRegistration.getEmail(), bindingResult);

        if(bindingResult.hasErrors()) {
            return registrationFailure(bindingResult);
        }

        User user = UserMapper.mapToUser(userRegistration, Role.USER);
        user.setPassword( passwordEncoder.encode(user.getPassword()) );
        userService.saveUser(user);

        return registrationSuccess();
    }

    private ResponseEntity<Object> registrationSuccess() {
        Map<String, Object> payload = new HashMap<>();

        payload.put("message", "Registration success");

        return ResponseEntity.ok(payload);
    }

    private ResponseEntity<Object> registrationFailure(BindingResult bindingResult) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("message", "Registration failure");
        payload.put("errors", mapErrors(bindingResult.getFieldErrors()));

        return ResponseEntity.status(422).body(payload);
    }

    private Map<String, String> mapErrors(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

}
