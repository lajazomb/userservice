package bookstore.userservice.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private String address;
    @NonNull private String country;
    @NonNull private String city;
    @NonNull private String zipCode;

    @Enumerated(EnumType.STRING)
    @NonNull private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();

        claims.put("firstName", this.getFirstName());
        claims.put("lastName", this.getLastName());
        claims.put("address", this.getAddress());
        claims.put("country", this.getCountry());
        claims.put("city", this.getCity());
        claims.put("zipCode", this.getZipCode());
        claims.put("role", this.getRole().toString());

        return claims;
    }
}
