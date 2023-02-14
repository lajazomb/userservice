package bookstore.userservice.port.product.exception;

import java.util.UUID;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(UUID userId) {
        super("There is no user with the user ID " + userId);
    }

    public UserNotFoundException(boolean email, String opt) {
        super("There is no user with the " + (email ? "email" : "username") + " " + opt);
    }


}
