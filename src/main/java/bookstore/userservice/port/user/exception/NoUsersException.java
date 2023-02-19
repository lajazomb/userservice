package bookstore.userservice.port.user.exception;

public class NoUsersException extends Exception {

    public NoUsersException() {
        super("There are no registered users.");
    }
}
