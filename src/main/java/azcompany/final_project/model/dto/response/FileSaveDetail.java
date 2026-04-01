package azcompany.final_project.model.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveDetail {
    private String originalFilename;
    private String changedFilename;
    private String path;
}
