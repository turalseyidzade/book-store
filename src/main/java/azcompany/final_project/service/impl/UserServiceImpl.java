package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.UserMapper;
import azcompany.final_project.model.dto.request.UserUpdateRequest;
import azcompany.final_project.model.dto.response.UserResponse;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.repository.RefreshTokenRepository;
import azcompany.final_project.repository.UserRepository;
import azcompany.final_project.service.abstracts.UserService;
import azcompany.final_project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtil securityUtil;
    private final CartRepository cartRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserResponse getCurrentUser() {
        log.info("UserService.getCurrentUser.start");
        UserEntity user = securityUtil.getUser();
        UserResponse response = userMapper.toResponse(user);
        log.info("UserService.getCurrentUser.end");
        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("UserService.getUserById.start: {}", id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        UserResponse response = userMapper.toResponse(user);
        log.info("UserService.getUserById.end: {}", id);
        return response;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("UserService.getAllUsers.start");
        List<UserEntity> users = userRepository.findAll();
        List<UserResponse> response = users.stream()
                .map(userMapper::toResponse)
                .toList();
        log.info("UserService.getAllUsers.end");
        return response;
    }

    @Override
    public UserResponse updateUserById(UserUpdateRequest request) {
        log.info("UserService.updateUserById.start: {}", request);
        UserEntity user = securityUtil.getUser();
        user = userMapper.updateRequestToEntity(request, user);
        UserEntity savedEntity = userRepository.save(user);
        UserResponse response = userMapper.toResponse(savedEntity);
        log.info("UserService.updateUserById.end: {}", request);
        return response;
    }

    @Override
    @Transactional
    public void delete() {
        log.info("UserService.deleteUsers.start");
        UserEntity user = securityUtil.getUser();
        cartRepository.deleteByUser(user);
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
        log.info("UserService.deleteUsers.end");
    }
}
