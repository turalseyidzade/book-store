package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadResponse upload(MultipartFile file);
}
