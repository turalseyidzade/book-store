package azcompany.final_project.file;

import azcompany.final_project.model.dto.response.FileSaveDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalFileStorage implements FileStorage {

    @Override
    public FileSaveDetail store(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            String uploadDir = "files";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String changedFilename = UUID.randomUUID() + extension;
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            Files.copy(file.getInputStream(), root.resolve(changedFilename), StandardCopyOption.REPLACE_EXISTING);

            return FileSaveDetail.builder()
                    .originalFilename(originalFilename)
                    .changedFilename(changedFilename)
                    .path(root.resolve(changedFilename).toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }
}
