package api.library.Candle.controller;

import api.library.Candle.domain.exceptions.EmailAlreadyExistsException;
import api.library.Candle.domain.model.dto.*;
import api.library.Candle.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "usuario", description = "controlador para salvar e editar dados do usuário")
public class UserController {


    @Autowired
    private EmailVerificationService verificationService;

    @PostMapping("/send-recovery")
    public ResponseEntity<?> sendRecoveryVerificationEmail(@RequestBody @Valid RecoveryRequestDto recovery){
        verificationService.sendRecoveryVerification(
                recovery.email()
        );
        return ResponseEntity.ok("Código de recuperação enviado com sucesso!");
    }

    @PostMapping("/confirm-recovery")
    public ResponseEntity<?> confirmRecovery(@RequestBody @Valid ConfirmRecoveryDto dto) {
        verificationService.confirmRecovery(dto.email(), dto.token(), dto.newPassword());
        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }


    @PostMapping("/send-verification")
    public ResponseEntity<?> sendRegisterVerification(@RequestBody @Valid RegisterUserDto register) {
        verificationService.sendRegisterVerification(
                register.username(),
                register.email(),
                register.password()
        );
        return ResponseEntity.ok("Código de verificação enviado para o email.");
    }

    @PostMapping("/confirm-verification")
    public ResponseEntity<?> confirmVerification(@RequestBody RequestDto request) {
        try {
            CreatedUserDto createdUser = verificationService.confirmVerification(request.email(), request.code());
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}

