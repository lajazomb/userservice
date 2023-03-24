package bookstore.userservice.core.domain.service.interfaces;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;
import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;

public interface IAuthenticationService {

    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public AuthenticationResponse register(RegisterRequest request) throws UserEmailAlreadyExistsException, InvalidEmailException;

}
