package bookstore.userservice.port.user.controller;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;
import bookstore.userservice.core.domain.service.implementation.AuthenticationService;
import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws UserEmailAlreadyExistsException, InvalidEmailException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
