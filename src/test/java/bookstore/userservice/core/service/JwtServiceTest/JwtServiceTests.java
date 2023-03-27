package bookstore.userservice.core.service.JwtServiceTest;

import bookstore.userservice.core.domain.service.implementation.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtServiceTests {

    @Autowired
    private JwtService jwtService;

    @Test
    public void testGenerateTokenWithExtraClaimsAndUserDetails() {
        // Setup
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "admin");
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());

        // Verify
        String token = jwtService.generateToken(extraClaims, userDetails);
        assertNotNull(token);
    }

    @Test
    public void testGenerateTokenWithUserDetails() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());

        // Verify
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    public void testIsTokenValidWithValidTokenAndUserDetails() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Verify
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenValidWithInvalidTokenAndUserDetails() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Verify
        boolean isValid = jwtService.isTokenValid(token, new User("invaliduser", "invalidpassword", new ArrayList<>()));
        assertFalse(isValid);
    }

    @Test
    public void testExtractUsernameWithValidToken() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Verify
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    public void testExtractExpirationWithValidToken() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Verify
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
    }

    @Test
    public void testIsTokenExpiredWithValidToken() {
        // Setup
        UserDetails userDetails = new User("testuser", "testpassword", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Verify
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }
}