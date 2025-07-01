package api.library.Candle.service;

import api.library.Candle.domain.model.EmailVerificationToken;
import api.library.Candle.domain.model.User;
import api.library.Candle.domain.model.UserRole;
import api.library.Candle.domain.model.dto.CreatedUserDto;
import api.library.Candle.domain.model.dto.LoginRequestDto;
import api.library.Candle.infra.email.EmailService;
import api.library.Candle.infra.repository.EmailVerificationTokenRepository;
import api.library.Candle.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendRecoveryVerification(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("Se este e-mail estiver registrado, você receberá um código.");
        }
        String token = generateUnique6DigitToken();
        EmailVerificationToken recoveryVerificationToken = EmailVerificationToken.builder()
                .email(email)
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();
        tokenRepository.save(recoveryVerificationToken);

        emailService.sendEmail(email,
                "Código de recuperação - Candle App",
                "Seu código de recuperação é: " + token);
    }

    public void confirmRecovery(String email, String token, String newPassword) {
        Optional<EmailVerificationToken> optToken = tokenRepository.findByToken(token);

        if (optToken.isEmpty()) {
            throw new RuntimeException("Token inválido.");
        }

        EmailVerificationToken tokenEntity = optToken.get();

        if (!tokenEntity.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Token não pertence ao email informado.");
        }

        if (tokenEntity.isUsed()) {
            throw new RuntimeException("Token já utilizado.");
        }

        if (tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        String encryptedPassword = passwordEncoder.encode(newPassword);

        user.changePassword(encryptedPassword);

        userRepository.save(user);

        tokenEntity.setUsed(true);
        tokenRepository.save(tokenEntity);
    }



    public void sendRegisterVerification(String username, String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        String token = generateUnique6DigitToken();
        String encryptedPassword = passwordEncoder.encode(password);

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .email(email)
                .username(username)
                .password(encryptedPassword)
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(verificationToken);

        emailService.sendEmail(
                email,
                "Código de verificação - Candle App",
                "Seu código de verificação é: " + token
        );
    }

    public CreatedUserDto confirmVerification(String email, String token) {
        Optional<EmailVerificationToken> opt = tokenRepository.findByToken(token);

        if (opt.isEmpty()) {
            throw new RuntimeException("Token inválido.");
        }

        EmailVerificationToken verification = opt.get();

        if (!verification.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Token não pertence ao email informado.");
        }

        if (verification.isUsed()) {
            throw new RuntimeException("Token já utilizado.");
        }

        if (verification.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado.");
        }

        CreatedUserDto createdUser = createUserFromVerification(verification);

        verification.setUsed(true);
        tokenRepository.save(verification);

        return createdUser;
    }


    private CreatedUserDto createUserFromVerification(EmailVerificationToken verification) {
        String email = verification.getEmail().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        User user = new User(
                verification.getUsername(),
                verification.getPassword(), // Já está criptografada lá no sendVerification
                verification.getEmail(),
                UserRole.USER
        );

        userRepository.save(user);

        return new CreatedUserDto(user);
    }


    private String generate6DigitToken() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);  // número entre 100000 e 999999
        return String.valueOf(number);
    }
    private String generateUnique6DigitToken() {
        String token;
        do {
            token = generate6DigitToken();
        } while (tokenRepository.findByToken(token).isPresent());
        return token;
    }

}
