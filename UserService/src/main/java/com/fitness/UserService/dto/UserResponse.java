package com.fitness.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fitness.UserService.modelss.UserRole;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String keycloakId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
