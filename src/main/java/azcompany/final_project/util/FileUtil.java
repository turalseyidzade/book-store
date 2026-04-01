package azcompany.final_project.util;

import azcompany.final_project.model.entity.BaseFileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {
    public String getFileUrl(String changedFileName) {
        if (changedFileName == null || changedFileName.isEmpty()) {
            return null;
        }
        return "files/" + changedFileName;
    }

    public String getFileUrl(BaseFileEntity file) {
        if (file == null) {
            return null;
        }
        return getFileUrl(file.getChangedFileName());
    }
}
