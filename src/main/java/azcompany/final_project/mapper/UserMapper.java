package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.request.RegisterRequest;
import azcompany.final_project.model.dto.request.UserUpdateRequest;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(RegisterRequest request) {
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity updateRequestToEntity(UserUpdateRequest request, UserEntity entity) {
        if (request.getFirstName() != null) {
            entity.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            entity.setLastName(request.getLastName());
        }
        return entity;
    }
}
