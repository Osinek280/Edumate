package com.edumate.edumate.services;

import com.edumate.edumate.config.JwtService;
import com.edumate.edumate.controllers.auth.AuthenticationResponse;
import com.edumate.edumate.controllers.auth.LoginRequest;
import com.edumate.edumate.controllers.auth.RegisterRequest;
import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.entities.user.Role;
import com.edumate.edumate.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    AppUser user = new AppUser();
    user.setEmail(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ADMIN);

    repository.save(user);

    String token = jwtService.generateToken(user.getEmail(), user.getRole());
    return AuthenticationResponse.builder()
        .token(token)
        .build();
  }

  public AuthenticationResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    AppUser user = repository.findByEmail(request.getEmail())
        .orElseThrow();

    String token = jwtService.generateToken(user.getEmail(), user.getRole());

    return AuthenticationResponse.builder()
        .token(token)
        .build();
  }
}
