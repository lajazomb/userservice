package bookstore.userservice.port.user.advice;

import bookstore.userservice.port.user.exception.UserUsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserUsernameAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(value = UserUsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userUsernameAlreadyExistsHandler(UserUsernameAlreadyExistsException exception){
        return exception.getMessage();
    }

}
