package api.library.Candle.controller;

import api.library.Candle.domain.model.dto.LoginRequestDto;
import api.library.Candle.domain.model.dto.LoginResponseDto;
import api.library.Candle.domain.model.dto.RefreshTokenRequestDto;
import api.library.Candle.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshRequest) {
        LoginResponseDto response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }
}
