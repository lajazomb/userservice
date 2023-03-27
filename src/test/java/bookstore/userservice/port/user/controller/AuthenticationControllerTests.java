package bookstore.userservice.port.user.controller;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;
import bookstore.userservice.core.domain.service.implementation.AuthenticationService;
import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTests {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegister() throws UserEmailAlreadyExistsException, InvalidEmailException {
        // Setup
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe@example.com", "password123", "123 Main St", "USA", "New York", "10001");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token", false);

        // Stubbing
        when(authenticationService.register(request)).thenReturn(expectedResponse);

        // Execute
        ResponseEntity<AuthenticationResponse> response = authenticationController.register(request);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testAuthenticate() {
        // Setup
        AuthenticationRequest request = new AuthenticationRequest("email@example.com", "password");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token", false);

        // Stubbing
        when(authenticationService.authenticate(request)).thenReturn(expectedResponse);

        // Execute
        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(request);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}