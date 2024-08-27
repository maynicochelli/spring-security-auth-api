package com.project.spring_security_auth_api.login.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.spring_security_auth_api.login.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
}
