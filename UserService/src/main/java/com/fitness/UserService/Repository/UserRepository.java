package com.fitness.UserService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.UserService.modelss.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByKeycloakId(String keycloakId);
    java.util.Optional<User> findByKeycloakId(String keycloakId);
    boolean existsById(Long id);

}
