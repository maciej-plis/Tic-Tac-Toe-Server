package matthias.tictactoe.authentication;

import matthias.tictactoe.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<Object> handlerUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(null, null, NOT_FOUND);
    }
}
