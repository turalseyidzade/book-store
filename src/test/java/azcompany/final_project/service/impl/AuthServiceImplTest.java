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
import azcompany.final_project.model.entity.CartEntity;
import azcompany.final_project.model.entity.RefreshTokenEntity;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.model.enums.Role;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.repository.RefreshTokenRepository;
import azcompany.final_project.repository.UserRepository;
import azcompany.final_project.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private RefreshTokenMapper refreshTokenMapper;
    @Mock private CartRepository cartRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("Tural", "Seyidzade", "tural@gmail.com", "password123");
        userEntity = UserEntity.builder()
                .email("tural@gmail.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
        userResponse = new UserResponse(1L, "Tural", "Seyidzade", "tural@gmail.com", Role.USER);

        ReflectionTestUtils.setField(authService, "refreshTokenExpiration", 3600000L);
    }

    @Test
    void register_Success_WithRoleUser_ShouldCreateCart() {
        when(userMapper.toEntity(registerRequest)).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        UserResponse result = authService.register(registerRequest, Role.USER);

        assertNotNull(result);
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository).save(any(UserEntity.class));
        verify(cartRepository).save(any(CartEntity.class));
    }

    @Test
    void register_Success_WithRoleAdmin_ShouldNotCreateCart() {
        userEntity.setRole(Role.ADMIN);
        when(userMapper.toEntity(registerRequest)).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        authService.register(registerRequest, Role.ADMIN);

        verify(cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void login_Success_ShouldReturnTokens() {
        LoginRequest loginRequest = new LoginRequest("tural@gmail.com", "password123");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generateToken(anyString(), any(Role.class))).thenReturn("access_token");
        when(refreshTokenRepository.findByUser(userEntity)).thenReturn(Optional.empty());

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        when(refreshTokenMapper.toEntity(any(), any())).thenReturn(refreshTokenEntity);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        verify(authenticationManager).authenticate(any());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void login_UserNotFound_ShouldThrowNotFoundException() {
        LoginRequest loginRequest = new LoginRequest("wrong@gmail.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void refreshToken_Success_ShouldReturnNewTokens() {
        UUID oldToken = UUID.randomUUID();
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest(oldToken);
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(userEntity);

        when(refreshTokenRepository.findByRefreshToken(oldToken)).thenReturn(Optional.of(entity));
        when(jwtUtil.generateToken(anyString(), any())).thenReturn("new_access_token");

        TokenRefreshResponse response = authService.refreshToken(refreshRequest);

        assertNotNull(response);
        assertEquals("new_access_token", response.getAccessToken());
        assertNotEquals(oldToken, response.getRefreshToken());
        verify(refreshTokenRepository).save(entity);
    }
}