package matthias.tictactoe.authentication;

import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.exception.UserNotFoundException;
import matthias.tictactoe.user.dto.UserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static java.util.stream.Collectors.toList;

@Configuration
class AuthenticationConfiguration {

    @Bean
    UserDetailsService userDetailsService(UserFacade userFacade) {
        return username -> userFacade.findUserByUsername(username)
            .map(this::toUserDetails)
            .orElseThrow(UserNotFoundException::new);
    }

    private UserDetails toUserDetails(UserDto user) {
        return new User(
            user.username(),
            "",
            user.roles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(toList())
        );
    }
}
