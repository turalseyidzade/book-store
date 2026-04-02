package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.request.LoginRequest;
import azcompany.final_project.model.dto.request.RegisterRequest;
import azcompany.final_project.model.dto.request.TokenRefreshRequest;
import azcompany.final_project.model.dto.response.LoginResponse;
import azcompany.final_project.model.dto.response.TokenRefreshResponse;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.enums.Role;

public interface AuthService {
    UserResponse register(RegisterRequest request, Role role);

    LoginResponse login(LoginRequest request);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}
