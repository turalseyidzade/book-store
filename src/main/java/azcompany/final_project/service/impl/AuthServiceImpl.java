package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.RefreshTokenMapper;
import azcompany.final_project.mapper.UserMapper;
import azcompany.final_project.model.dto.request.LoginRequest;
import azcompany.final_project.model.dto.request.RegisterRequest;
import azcompany.final_project.model.dto.request.TokenRefreshRequest;
import azcompany.final_project.model.dto.response.LoginResponse;
import azcompany.final_project.model.dto.response.TokenRefreshResponse;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.entity.RefreshTokenEntity;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.model.enums.Role;
import azcompany.final_project.repository.RefreshTokenRepository;
import azcompany.final_project.repository.UserRepository;
import azcompany.final_project.service.abstracts.AuthService;
import azcompany.final_project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public UserResponse register(RegisterRequest request, Role role) {
        log.info("AuthService.register.start: {}", request);
        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setRole(role);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        UserEntity savedEntity = userRepository.save(userEntity);
        log.info("AuthService.register.end");
        return userMapper.toResponse(savedEntity);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("AuthService.login.start: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        String accessToken = jwtUtil.generateToken(userEntity.getEmail(), userEntity.getRole());
        UUID refreshToken = UUID.randomUUID();

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUser(userEntity)
                .map(foundRefreshTokenEntity -> {
                    foundRefreshTokenEntity.setRefreshToken(refreshToken);
                    foundRefreshTokenEntity.setExpireDate(LocalDateTime.now().plus(300000, ChronoUnit.MILLIS));
                    foundRefreshTokenEntity.setRevoked(false);
                    return foundRefreshTokenEntity;
                })
                .orElseGet(() -> refreshTokenMapper.toEntity(userEntity, refreshToken));
        refreshTokenRepository.save(refreshTokenEntity);
        log.info("AuthService.login.end");
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        log.info("AuthService.refreshToken.start: {}", request);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));
        jwtUtil.checkRefreshToken(refreshTokenEntity);

        UserEntity userEntity = refreshTokenEntity.getUser();
        String newAccessToken = jwtUtil.generateToken(userEntity.getEmail(), userEntity.getRole());
        UUID newRefreshToken = UUID.randomUUID();

        refreshTokenEntity.setRefreshToken(newRefreshToken);
        refreshTokenEntity.setExpireDate(LocalDateTime.now().plus(300000, ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshTokenEntity);

        TokenRefreshResponse tokenRefreshResponse = TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        log.info("AuthService.refreshToken.end");
        return tokenRefreshResponse;
    }
}
