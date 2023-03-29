package bookstore.userservice.port.user.controller;

import bookstore.userservice.core.domain.model.AuthenticationRequest;
import bookstore.userservice.core.domain.model.AuthenticationResponse;
import bookstore.userservice.core.domain.model.RegisterRequest;
import bookstore.userservice.core.domain.service.interfaces.IAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AuthenticationControllerTests {

    private MockMvc mockMvc;

    @Mock
    private IAuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private static final String BASE_URL = "/api/v1/auth/";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void registerUser() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .address("123 Main St")
                .country("US")
                .city("Anytown")
                .zipCode("12345")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("token")
                .isAdmin(false)
                .build();
        given(authenticationService.register(request)).willReturn(response);

        mockMvc.perform(post(BASE_URL + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.getToken()))
                .andExpect(jsonPath("$.admin").value(response.isAdmin()));
    }

    @Test
    public void authenticateUser() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("token")
                .isAdmin(false)
                .build();
        given(authenticationService.authenticate(request)).willReturn(response);

        mockMvc.perform(post(BASE_URL + "authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.getToken()))
                .andExpect(jsonPath("$.admin").value(response.isAdmin()));
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}