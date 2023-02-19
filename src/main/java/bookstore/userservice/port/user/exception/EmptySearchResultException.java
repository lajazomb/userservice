package bookstore.userservice.port.user.exception;

public class EmptySearchResultException extends Exception {
    public EmptySearchResultException () {
        super("Search result is empty.");
    }
}
