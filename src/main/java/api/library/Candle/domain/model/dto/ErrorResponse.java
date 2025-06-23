package api.library.Candle.domain.model.dto;

public record ErrorResponse(String message, int statusCode, String timestamp) {
}
