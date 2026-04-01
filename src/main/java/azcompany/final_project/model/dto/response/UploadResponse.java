package azcompany.final_project.model.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private Long id;
    private String originalFileName;
    private String changedFileName;
    private String url;
}
