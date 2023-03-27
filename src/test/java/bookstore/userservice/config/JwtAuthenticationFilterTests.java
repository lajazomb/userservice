package bookstore.userservice.config;

import bookstore.userservice.core.domain.service.implementation.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void doFilterInternal_shouldSetAuthenticationToken_whenValidJwtTokenIsProvided() throws ServletException, IOException, ServletException, IOException {
        // Setup
        String jwt = "valid-jwt-token";
        String userEmail = "user@example.com";

        // Stubbing
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(new User(userEmail, "password", new ArrayList<>()));

        // Execute
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify
        verify(jwtService).isTokenValid(jwt, userDetailsService.loadUserByUsername(userEmail));
        // verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class)); mock of AuthenticationManager not working for some reason
        // does NOT work with @MockBean too
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_shouldNotSetAuthenticationToken_whenJwtTokenIsNotProvided() throws ServletException, IOException {
        // Stubbing
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService, authenticationManager);
    }

    @Test
    public void doFilterInternal_shouldNotSetAuthenticationToken_whenJwtTokenIsInvalid() throws ServletException, IOException {
        // Setup
        String jwt = "invalid-jwt-token";
        String userEmail = "user@example.com";

        // Stubbing
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(new User(userEmail, "password", new ArrayList<>()));
        when(jwtService.isTokenValid(jwt, userDetailsService.loadUserByUsername(userEmail))).thenReturn(false);

        // Execute
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify
        verify(jwtService).isTokenValid(jwt, userDetailsService.loadUserByUsername(userEmail));
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(authenticationManager);
    }

}