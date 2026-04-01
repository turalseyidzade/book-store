package azcompany.final_project.file;

import azcompany.final_project.model.dto.response.FileSaveDetail;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    FileSaveDetail store(MultipartFile file);
}
