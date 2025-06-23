package api.library.Candle.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password; // senha já criptografada

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime expiration;

    private boolean used = false;
}
