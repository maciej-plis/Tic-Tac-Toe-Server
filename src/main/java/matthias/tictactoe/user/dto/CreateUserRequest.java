package matthias.tictactoe.user.dto;

import lombok.Builder;
import lombok.Data;
import matthias.tictactoe.user.dto.validators.annotations.UniqueEmail;
import matthias.tictactoe.user.dto.validators.annotations.UniqueUsername;
import matthias.tictactoe.user.dto.validators.annotations.ValidPassword;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
public class CreateUserRequest {

    @UniqueUsername
    @Length(min = 5, max = 16, message = "Username must be from 5 up to 16 characters long")
    @Pattern(regexp = "^\\w*$", message = "Username can only contain letters, numbers and underscores")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @UniqueEmail
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @ValidPassword
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
