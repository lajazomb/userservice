package bookstore.userservice.core.domain.service.interfaces;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;

public interface IAuthenticationService {

    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public AuthenticationResponse register(RegisterRequest request);

}
