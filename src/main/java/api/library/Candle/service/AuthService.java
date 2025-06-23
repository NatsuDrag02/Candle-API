package api.library.Candle.service;
import api.library.Candle.domain.model.dto.LoginRequestDto;
import api.library.Candle.domain.model.dto.LoginResponseDto;
import api.library.Candle.domain.model.dto.RefreshTokenRequestDto;
import api.library.Candle.infra.security.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDto login(LoginRequestDto request) {
        String email = request.email().toLowerCase();
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );


            String accessToken = jwtUtil.generateAccessToken(request.email());
            String refreshToken = jwtUtil.generateRefreshToken(request.email());

            return new LoginResponseDto(accessToken, refreshToken);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou senha inválidos.");
        }
    }

    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.refreshToken();

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);

        return new LoginResponseDto(newAccessToken, refreshToken);
    }
}