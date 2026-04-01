package azcompany.final_project.service.impl;

import azcompany.final_project.file.FileStorage;
import azcompany.final_project.mapper.FileMapper;
import azcompany.final_project.model.dto.response.FileSaveDetail;
import azcompany.final_project.model.dto.response.UploadResponse;
import azcompany.final_project.model.entity.BaseFileEntity;
import azcompany.final_project.repository.BaseFileRepository;
import azcompany.final_project.service.abstracts.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileStorage fileStorage;
    private final BaseFileRepository baseFileRepository;
    private final FileMapper fileMapper;

    @Override
    public UploadResponse upload(MultipartFile file) {
        FileSaveDetail detail = fileStorage.store(file);

        BaseFileEntity baseFile = saveFile(detail);
        return fileMapper.toResponse(baseFile);
    }

    private BaseFileEntity saveFile(FileSaveDetail detail) {
        return baseFileRepository.save(BaseFileEntity.builder()
                .changedFileName(detail.getChangedFilename())
                .originalName(detail.getOriginalFilename())
                .build()
        );
    }
}
