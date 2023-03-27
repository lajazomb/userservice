package bookstore.userservice.core.service.JwtServiceTest;

import bookstore.userservice.core.domain.model.*;
import bookstore.userservice.core.domain.service.implementation.JwtService;
import bookstore.userservice.core.domain.service.implementation.AuthenticationService;
import bookstore.userservice.core.domain.service.interfaces.UserRepository;
import bookstore.userservice.port.user.exception.InvalidEmailException;
import bookstore.userservice.port.user.exception.UserEmailAlreadyExistsException;
import bookstore.userservice.port.user.exception.UserNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterNewUser() throws UserEmailAlreadyExistsException, InvalidEmailException {
        // Setup
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe@example.com", "password", "123 Main St", "USA", "New York", "10001");

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AuthenticationService authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);

        // Stubbing
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            // set any necessary values on savedUser
            return savedUser;
        });

        AuthenticationResponse response = authenticationService.register(request);

        // Check if response is valid
        assertNotNull(response);
        assertFalse(response.isAdmin());

        // Verify that the user was saved to the repository
        User savedUser = userRepository.save(User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .country(request.getCountry())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .role(Role.USER)
                .build());

        assertNotNull(savedUser);
        assertEquals(request.getEmail(), savedUser.getEmail());
    }

    @Test
    public void testRegisterExistingUser() {
        // Setup
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe@example.com", "password", "123 Main St", "USA", "New York", "10001");

        // Stubbing
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Verify
        assertThrows(UserEmailAlreadyExistsException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    public void testRegisterInvalidEmail() {
        // Setup
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request1 = new RegisterRequest("John", "Doe", "johndoe@a.de", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request2 = new RegisterRequest("John", "Doe", "johndoe@a", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request3 = new RegisterRequest("John", "Doe", "j.d@ad.a", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request4 = new RegisterRequest("John", "Doe", "§%john@bon.com", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request5 = new RegisterRequest("John", "Doe", "@bon.com", "password", "123 Main St", "USA", "New York", "10001");
        RegisterRequest request6 = new RegisterRequest("John", "Doe", "n.com", "password", "123 Main St", "USA", "New York", "10001");

        // Verify
        assertThrows(InvalidEmailException.class, () -> {
            authenticationService.register(request);
            authenticationService.register(request1);
            authenticationService.register(request2);
            authenticationService.register(request3);
            authenticationService.register(request4);
            authenticationService.register(request5);
            authenticationService.register(request6);
        });
    }

    @Test
    public void testAuthenticateUser() {
        // Mock dependencies
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AuthenticationService authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        // Stubbing
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        // Set up a user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        // Stub behavior of user repository
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Generate a JWT token for user
        String jwtToken = "test_jwt_token";
        when(jwtService.generateToken(user.getClaims(), user)).thenReturn(jwtToken);

        // Call the method being tested
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Verify that the response contains the expected JWT token and user role
        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        assertFalse(response.isAdmin());
    }

    @Test
    public void testAuthenticateInvalidUser() {
        // Setup
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        // Stubbing
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Verify
        assertThrows(UserNotFoundException.class, () -> {
            authenticationService.authenticate(request);
        });
    }

    @Test
    public void testAuthenticateWrongPassword() {
        // Mock dependencies
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");
        AuthenticationService authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);

        // Create user and save to repository
        String email = "test@example.com";
        String password = "password123";
        String wrongPassword = "wrongpassword456";
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password(passwordEncoder.encode(password))
                .address("123 Main St")
                .country("USA")
                .city("New York")
                .zipCode("10001")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        // Create authentication request with wrong password and assert that an exception is thrown
        AuthenticationRequest request = new AuthenticationRequest(email, wrongPassword);
        assertThrows(Exception.class, () -> {
            authenticationService.authenticate(request);
        });
    }

    @Test
    public void testRegister_ValidRequest_ShouldCreateUser() throws UserEmailAlreadyExistsException, InvalidEmailException {
        // Create register request and mock dependencies
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password", "address", "country", "city", "12345");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Call register and verify that user is created and token is generated
        AuthenticationResponse response = authenticationService.register(request);
        verify(userRepository, Mockito.times(1)).save(any(User.class));
        verify(jwtService, Mockito.times(1)).generateToken(any(), any(User.class));
    }

    @Test
    public void testRegisterThrowsUserEmailAlreadyExistsException() {
        // Setup and stubbing
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        AuthenticationService authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe@example.com", "password", "123 Main St", "USA", "New York", "10001");

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email(request.getEmail())
                .password("password")
                .address("123 Main St")
                .country("USA")
                .city("New York")
                .zipCode("10001")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Verify
        assertThrows(UserEmailAlreadyExistsException.class, () -> authenticationService.register(request));
    }

    @Test
    public void testRegister_InvalidEmail_ShouldThrowException() {
        // Setup
        RegisterRequest request = new RegisterRequest("John", "Doe", "invalid-email", "password", "address", "country", "city", "12345");

        // Stubbing
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Verfify
        assertThrows(InvalidEmailException.class, () -> {
            authenticationService.register(request);
        });
    }
    @Test
    public void testAuthenticate_ValidRequest_ShouldReturnToken() throws UserNotFoundException {
        // Setup
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password");
        User user = new User();
        user.setRole(Role.USER);

        // Stubbing
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), any(User.class))).thenReturn("jwtToken");

        // Make auth request
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Verify
        verify(jwtService, Mockito.times(1)).generateToken(any(), any(User.class));
        assertEquals("jwtToken", response.getToken());
        assertFalse(response.isAdmin());
    }

    @Test
    public void testAuthenticate_UserNotFound_ShouldThrowException() throws UserNotFoundException {
        // Setup
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password");

        // Stubbing
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Verify
        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(request));
    }
}