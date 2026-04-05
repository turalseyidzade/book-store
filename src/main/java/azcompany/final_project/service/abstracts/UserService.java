package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.request.UserUpdateRequest;
import azcompany.final_project.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUser();

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUserById(UserUpdateRequest request);

    void delete();
}
