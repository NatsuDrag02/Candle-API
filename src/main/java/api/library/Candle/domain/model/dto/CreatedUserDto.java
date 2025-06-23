package api.library.Candle.domain.model.dto;

import api.library.Candle.domain.model.User;

import java.util.UUID;

public record CreatedUserDto(
        UUID id,
        String username,
        String email
) {
    public CreatedUserDto (User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }
}
