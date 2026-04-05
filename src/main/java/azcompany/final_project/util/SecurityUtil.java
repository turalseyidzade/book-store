package azcompany.final_project.util;

import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.model.enums.Role;
import azcompany.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    public UserEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access Denied");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    public void validateAccess(UserEntity target) {
        UserEntity currentUser = getUser();
        boolean isOwner = currentUser.getId().equals(target.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
