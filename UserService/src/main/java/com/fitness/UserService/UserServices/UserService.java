package com.fitness.UserService.UserServices;

import org.springframework.stereotype.Service;
import com.fitness.UserService.Repository.UserRepository;
import com.fitness.UserService.dto.RegisterRequest;
import com.fitness.UserService.dto.UserResponse;
import com.fitness.UserService.modelss.User;
import com.fitness.UserService.modelss.UserRole;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByKeycloakId(request.getKeycloakId())) {
            throw new RuntimeException("User already synced");
        }
        User user = new User();
        user.setKeycloakId(request.getKeycloakId());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.USER); // Default role
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setKeycloakId(savedUser.getKeycloakId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setRole(savedUser.getRole());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    } // ← cierre registerUser

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setRole(user.getRole());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    } // ← cierre getUserProfile

    public boolean existByUserId(String userId) {
        log.info("Calling user service to check existence for keycloakId: {}", userId);
        return userRepository.existsByKeycloakId(userId);
    } // ← cierre existByUserId

} // ← cierre clase
