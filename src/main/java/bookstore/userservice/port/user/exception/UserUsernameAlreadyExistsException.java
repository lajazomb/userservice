package bookstore.userservice.port.user.exception;

public class UserUsernameAlreadyExistsException extends Exception {

    public UserUsernameAlreadyExistsException(String username) {
        super("A user with the username " + username + " already exists.");
    }
}
