package bookstore.userservice.core.domain.service.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface IJwtService {

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public String generateToken(UserDetails userDetails);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public String extractUsername(String token);


}
