package azcompany.final_project.service.impl;

import azcompany.final_project.file.FileStorage;
import azcompany.final_project.mapper.FileMapper;
import azcompany.final_project.model.dto.response.FileSaveDetail;
import azcompany.final_project.model.dto.response.UploadResponse;
import azcompany.final_project.model.entity.BaseFileEntity;
import azcompany.final_project.repository.BaseFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FileStorage fileStorage;

    @Mock
    private BaseFileRepository baseFileRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void upload_ShouldReturnUploadResponse_WhenFileIsValid() {
        MultipartFile multipartFile = mock(MultipartFile.class);

        FileSaveDetail detail = FileSaveDetail.builder()
                .changedFilename("unique-name.jpg")
                .originalFilename("test.jpg")
                .build();

        BaseFileEntity baseFile = BaseFileEntity.builder()
                .changedFileName("unique-name.jpg")
                .originalName("test.jpg")
                .build();

        UploadResponse expectedResponse = new UploadResponse();
        expectedResponse.setChangedFileName("unique-name.jpg");

        when(fileStorage.store(multipartFile)).thenReturn(detail);
        when(baseFileRepository.save(any(BaseFileEntity.class))).thenReturn(baseFile);
        when(fileMapper.toResponse(baseFile)).thenReturn(expectedResponse);

        UploadResponse actualResponse = fileService.upload(multipartFile);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getChangedFileName(), actualResponse.getChangedFileName());

        verify(fileStorage).store(multipartFile);
        verify(baseFileRepository).save(any(BaseFileEntity.class));
        verify(fileMapper).toResponse(baseFile);
    }
}