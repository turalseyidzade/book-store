package azcompany.final_project.controller;

import azcompany.final_project.model.dto.request.UserUpdateRequest;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.service.abstracts.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserById(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        userService.delete();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
