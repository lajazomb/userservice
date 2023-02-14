package bookstore.userservice.port.product.advice;

import bookstore.userservice.port.product.exception.NoProductsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoProductsAdvice {

    @ResponseBody
    @ExceptionHandler(value = NoProductsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String productNotFoundHandler(NoProductsException exception){
        return exception.getMessage();
    }

}
