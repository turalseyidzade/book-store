package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.UserMapper;
import azcompany.final_project.model.dto.request.UserUpdateRequest;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.repository.RefreshTokenRepository;
import azcompany.final_project.repository.UserRepository;
import azcompany.final_project.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@gmail.com");
    }

    @Test
    void getCurrentUser_Success() {
        when(securityUtil.getUser()).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(securityUtil).getUser();
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_Success() {
        List<UserEntity> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(any())).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateUserById_Success() {
        UserUpdateRequest request = new UserUpdateRequest();
        when(securityUtil.getUser()).thenReturn(user);
        when(userMapper.updateRequestToEntity(request, user)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUserById(request);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void delete_Success() {
        when(securityUtil.getUser()).thenReturn(user);

        userService.delete();

        verify(cartRepository).deleteByUser(user);
        verify(refreshTokenRepository).deleteByUser(user);
        verify(userRepository).delete(user);
    }
}