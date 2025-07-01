package api.library.Candle.domain.model;

import api.library.Candle.domain.model.dto.RegisterUserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name= "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (role) {
            case ADMIN -> {return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));}
            case USER -> {return List.of(new SimpleGrantedAuthority("ROLE_USER"));}
            default -> {return null;}
        }
    }

    public User(String username, String encodedPassword, String email, UserRole role) {
        this.username = username;
        this.password = encodedPassword;
        this.email = email;
        this.role = role;
    }
        public void changePassword(String newEncryptedPassword) {
            this.password = newEncryptedPassword;
        }
    }
