package matthias.tictactoe.web.authentication.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.web.authentication.model.ResponseEntityBuilder;
import matthias.tictactoe.web.authentication.utils.JWTUtils;
import matthias.tictactoe.web.authentication.utils.UserMapper;
import matthias.tictactoe.web.authentication.validators.UserRegistrationValidator;
import matthias.tictactoe.web.authentication.model.Role;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserCredentials;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;
import matthias.tictactoe.web.authentication.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/register")
    public ResponseEntity createNewUser(@Valid @RequestBody UserRegistration userRegistration, BindingResult bindingResult) {

        registrationValidator.isUsernameAlreadyRegistered(userRegistration.getUsername(), bindingResult);
        registrationValidator.isEmailAlreadyRegistered(userRegistration.getEmail(), bindingResult);

        if(bindingResult.hasErrors()) {
            return ResponseEntityBuilder
                    .status(200)
                    .addToPayload("success", false)
                    .addToPayload("fieldErrors", fieldErrorsToStringMap(bindingResult.getFieldErrors()))
                    .build();
        }

        User user = UserMapper.mapToUserWithRoles(userRegistration, Role.USER);
        user.setPassword( passwordEncoder.encode(user.getPassword()) );
        userService.saveUser(user);

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Registration success")
                .build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticateUser(@RequestBody UserCredentials userCredentials) {

        User user = userService.findUserByUsername(userCredentials.getUsername());

        if(user == null || !passwordEncoder.matches(userCredentials.getPassword(), user.getPassword())) {
            return ResponseEntityBuilder
                    .status(200)
                    .addToPayload("success", false)
                    .addToPayload("message", "Incorrect username or password")
                    .build();
        }

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Authentication success")
                .addToPayload("user", new Object() {
                    public String username = user.getUsername();
                    public String token = JWTUtils.generateJWTForUser(user);
                })
                .build();
    }

    private Map<String, String> fieldErrorsToStringMap(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

}
