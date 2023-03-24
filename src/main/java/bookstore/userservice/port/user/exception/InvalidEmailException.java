package bookstore.userservice.port.user.exception;

public class InvalidEmailException extends Exception {

    public InvalidEmailException() {
        super("Invalid E-Mail.");
    }
}
