package azcompany.final_project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends BaseEntity {
    private UUID refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    private Boolean revoked;
    private LocalDateTime expireDate;
}
