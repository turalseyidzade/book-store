package azcompany.final_project.controller;

import azcompany.final_project.model.dto.UploadResponse;
import azcompany.final_project.service.abstracts.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.upload(file));
    }
}
