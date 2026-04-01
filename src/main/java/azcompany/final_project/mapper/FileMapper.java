package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.UploadResponse;
import azcompany.final_project.model.entity.BaseFileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public UploadResponse toResponse(BaseFileEntity file) {
        return UploadResponse.builder()
                .id(file.getId())
                .originalFileName(file.getOriginalName())
                .changedFileName(file.getChangedFileName())
                .url("files/" + file.getChangedFileName())
                .build();
    }
}
