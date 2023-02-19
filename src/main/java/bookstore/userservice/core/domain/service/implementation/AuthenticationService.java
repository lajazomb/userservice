package bookstore.userservice.core.domain.service.implementation;

import bookstore.userservice.core.domain.model.*;
import bookstore.userservice.core.domain.service.interfaces.IAuthenticationService;
import bookstore.userservice.core.domain.service.interfaces.UserRepository;
import bookstore.userservice.port.product.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user); // no extra claims

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // user is correct when here
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException());

        // generate Token
        var jwtToken = jwtService.generateToken(user); // no extra claims
        // send Token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
