package api.library.Candle.domain.model.dto;

import api.library.Candle.domain.model.User;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto (
        @NotBlank
        String email,
        @NotBlank
        String password) {

}

