package api.library.Candle.domain.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmRecoveryDto(@NotBlank String email, @NotBlank String token,@NotBlank String newPassword) {
}
