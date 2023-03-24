package bookstore.userservice.core.domain.service.implementation;

import bookstore.userservice.core.domain.model.*;
import bookstore.userservice.core.domain.service.interfaces.IAuthenticationService;
import bookstore.userservice.core.domain.service.interfaces.UserRepository;
import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;
import bookstore.userservice.port.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UserEmailAlreadyExistsException, InvalidEmailException {
        String email = request.getEmail();
        if (!repository.findByEmail(email).isEmpty()) {
            throw new UserEmailAlreadyExistsException(email);
        }

        if (!isValidEmail(email)) {
            throw new InvalidEmailException();
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .country(request.getCountry())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .role(Role.USER)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user.getClaims(), user); // no extra claims

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .isAdmin(user.getRole() == Role.ADMIN)
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
        var jwtToken = jwtService.generateToken(user.getClaims(), user);
        // send Token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .isAdmin(user.getRole() == Role.ADMIN)
                .build();
    }

    private boolean isValidEmail(String email) {
        String regex = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
        return email.matches(regex);
    }
}
