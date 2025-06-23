package com.edumate.edumate.services;

import com.edumate.edumate.config.JwtService;
import com.edumate.edumate.controllers.auth.AuthenticationResponse;
import com.edumate.edumate.controllers.auth.LanguageLevel;
import com.edumate.edumate.controllers.auth.RegisterRequest;
import com.edumate.edumate.dto.auth.LoginRequest;
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

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    AppUser user = new AppUser();
    user.setEmail(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);

    userRepository.save(user);

    String token = jwtService.generateToken(user.getEmail(), user.getRole());
    return AuthenticationResponse.builder()
        .token(token)
        .build();
  }

  public void updateLanguageLevel(String userEmail, LanguageLevel level) {
    AppUser user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));
    user.setLanguageLevel(level);
    userRepository.save(user);
  }


  public AuthenticationResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()
        )
    );

    AppUser user = userRepository.findByEmail(request.email())
        .orElseThrow();

    String token = jwtService.generateToken(user.getEmail(), user.getRole());

    return AuthenticationResponse.builder()
        .token(token)
        .build();
  }
}
