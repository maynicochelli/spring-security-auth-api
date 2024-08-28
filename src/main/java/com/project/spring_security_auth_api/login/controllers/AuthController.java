package com.project.spring_security_auth_api.login.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring_security_auth_api.login.domain.LoginRequestDto;
import com.project.spring_security_auth_api.login.domain.RegisterRequestDto;
import com.project.spring_security_auth_api.login.domain.ResponseDto;
import com.project.spring_security_auth_api.login.domain.User;
import com.project.spring_security_auth_api.login.repositories.UserRepository;
import com.project.spring_security_auth_api.login.security.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginRequestDto body) {
    User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));

    if(passwordEncoder.matches(body.password(), user.getPassword())) {
        String token = this.tokenService.generateToken(user);
        return ResponseEntity.ok(new ResponseDto(user.getName(), token));
    }
    return ResponseEntity.badRequest().build();
  }

  @PostMapping("/register")
  public ResponseEntity login(@RequestBody RegisterRequestDto body) {
    Optional<User> user = this.repository.findByEmail(body.email());

    // NEW USERS
    if (user.isEmpty()) {
      User newUser = new User();
      newUser.setPassword(passwordEncoder.encode(body.password()));
      newUser.setEmail(body.email());
      newUser.setName(body.name());
      this.repository.save(newUser);

      String token = this.tokenService.generateToken(newUser);
      return ResponseEntity.ok(new ResponseDto(newUser.getName(), token));
    }
    return ResponseEntity.badRequest().build();
  }

}
