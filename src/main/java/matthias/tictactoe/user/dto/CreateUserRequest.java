package matthias.tictactoe.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import matthias.tictactoe.user.dto.validators.annotations.UniqueEmail;
import matthias.tictactoe.user.dto.validators.annotations.UniqueUsername;
import matthias.tictactoe.user.dto.validators.annotations.ValidPassword;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateUserRequest(
    @UniqueUsername
    @Length(min = 5, max = 16, message = "Username must be from 5 up to 16 characters long")
    @Pattern(regexp = "^\\w*$", message = "Username can only contain letters, numbers and underscores")
    @NotBlank(message = "Username cannot be blank")
    String username,
    @UniqueEmail
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    String email,
    @ValidPassword
    @NotBlank(message = "Password cannot be blank")
    String password
) {
}
