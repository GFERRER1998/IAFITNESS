package com.fitness.UserService.modelss;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String keycloakId;
    
    private String name;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
