package matthias.tictactoe.web.authentication.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import matthias.tictactoe.web.authentication.validators.annotations.FieldsValueMatch;
import matthias.tictactoe.web.authentication.validators.annotations.UniqueEmail;
import matthias.tictactoe.web.authentication.validators.annotations.UniqueUsername;
import matthias.tictactoe.web.authentication.validators.annotations.ValidPassword;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@FieldsValueMatch(
    field = "password",
    fieldMatch = "verifyPassword",
    message = "Passwords do not match"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegistration {
    @Length(min = 5, max = 16, message = "Your username must be 5-16 characters")
    @Pattern(regexp = "^[\\w]*$", message = "The username can only contain letters, numbers and underscore")
    @NotEmpty(message = "Please provide a username")
    @UniqueUsername
    private String username;

    @Email(message = "Please provide a valid Email")
    @NotEmpty(message = "Please provide an email")
    @UniqueEmail
    private String email;

    @ValidPassword
    @NotEmpty(message = "Please provide your password")
    private String password;

    private String verifyPassword;

}
