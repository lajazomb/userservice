package bookstore.userservice.port.product.exception;

public class EmptySearchResultException extends Exception {
    public EmptySearchResultException () {
        super("Search result is empty.");
    }
}
