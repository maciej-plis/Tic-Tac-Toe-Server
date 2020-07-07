package matthias.tictactoe.web.authentication.controllers;

import matthias.tictactoe.web.authentication.helpers.UserMapper;
import matthias.tictactoe.web.authentication.model.User;
import matthias.tictactoe.web.authentication.model.dtos.UserRegistration;
import matthias.tictactoe.web.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/register")
    public ResponseEntity<Object> createNewUser(@Valid UserRegistration userRegistration, BindingResult bindingResult) {

        boolean usernameExists = userService.findUserByUsername(userRegistration.getUsername())  != null;
        boolean emailExists = userService.findUserByEmail(userRegistration.getEmail()) != null;

        if(usernameExists) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");
        }
        if(emailExists) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }

        Map<String, Object> payload = new HashMap<>();

        if(bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            payload.put("errors", errors);

            return ResponseEntity.status(422).body(payload);
        }

        User user = UserMapper.mapToUser(userRegistration);
        userService.saveUser(user);

        payload.put("successMessage", "User has been registered successfully");

        return ResponseEntity.ok(payload);
    }

}
