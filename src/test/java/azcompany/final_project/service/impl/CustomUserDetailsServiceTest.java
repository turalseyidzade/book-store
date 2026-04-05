package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.model.enums.Role;
import azcompany.final_project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserEntity mockUser;
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setEmail(email);
        mockUser.setPassword("encoded_password");
        mockUser.setRole(Role.USER);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        String nonExistentEmail = "notfound@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(nonExistentEmail);
        });

        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
    }
}