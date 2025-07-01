package api.library.Candle.domain.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RecoveryRequestDto(@NotBlank String email) {
}
