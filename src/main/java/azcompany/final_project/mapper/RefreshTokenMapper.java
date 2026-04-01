package azcompany.final_project.mapper;

import azcompany.final_project.model.entity.RefreshTokenEntity;
import azcompany.final_project.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class RefreshTokenMapper {
    public RefreshTokenEntity toEntity(UserEntity userEntity, UUID refreshToken) {
        return RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .user(userEntity)
                .revoked(false)
                .expireDate(LocalDateTime.now().plus(300000, ChronoUnit.MILLIS))
                .build();
    }
}
