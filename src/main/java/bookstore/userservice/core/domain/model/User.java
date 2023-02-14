package bookstore.userservice.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Getter private UUID userId;

    @Column(nullable = false)
    @Getter private String username;

    @Column(nullable = false)
    @Getter private String email;

    @Column(nullable = false)
    @Getter private String password; // TODO: Store only the hash

    @Column(nullable = false)
    @Getter private String address;

    @Column(nullable = false)
    @Getter private String zipCode;

    @Column(nullable = false)
    @Getter private String country;

    @Column(nullable = false)
    @Getter private String city;

}

