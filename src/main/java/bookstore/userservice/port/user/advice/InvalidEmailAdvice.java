package bookstore.userservice.port.user.advice;

import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidEmailAdvice {

    @ResponseBody
    @ExceptionHandler(value = InvalidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userEmailAlreadyExistsHandler(InvalidEmailException exception){
        return exception.getMessage();
    }

}
