package azcompany.final_project.model.dto.response;

import azcompany.final_project.model.enums.Role;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
