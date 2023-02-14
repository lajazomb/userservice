package bookstore.userservice.port.product.exception;

public class NoUsersException extends Exception {

    public NoUsersException() {
        super("There are no registered users.");
    }
}
