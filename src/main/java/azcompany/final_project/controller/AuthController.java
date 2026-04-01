package azcompany.final_project.controller;

import azcompany.final_project.model.dto.request.LoginRequest;
import azcompany.final_project.model.dto.request.RegisterRequest;
import azcompany.final_project.model.dto.response.LoginResponse;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.enums.Role;
import azcompany.final_project.service.abstracts.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody @Valid RegisterRequest request,
            @RequestParam Role role
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request, role));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(request));
    }
}
