package bookstore.userservice.port.product;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;
import bookstore.userservice.core.domain.service.implementation.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("AMOGUS 1");
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("AMOGUS 2");
        return ResponseEntity.ok(service.authenticate(request));
    }
}
