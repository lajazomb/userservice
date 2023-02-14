package bookstore.userservice.port.product.advice;

import bookstore.userservice.port.product.exception.NoUsersException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoUsersAdvice {

    @ResponseBody
    @ExceptionHandler(value = NoUsersException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(NoUsersException exception){
        return exception.getMessage();
    }

}
