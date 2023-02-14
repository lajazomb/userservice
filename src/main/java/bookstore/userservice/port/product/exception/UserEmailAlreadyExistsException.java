package bookstore.userservice.port.product.exception;

public class UserEmailAlreadyExistsException extends Exception {

    public UserEmailAlreadyExistsException(String email) {
        super("A user with the email " + email + " already exists.");
    }
}
