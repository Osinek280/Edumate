package com.edumate.edumate.controllers.auth;

import com.edumate.edumate.dto.auth.LoginRequest;
import com.edumate.edumate.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/login")
  @Operation(summary = "User Login", description = "Authenticates a user and returns a JWT token.")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody LoginRequest request
  ) {
    return ResponseEntity.ok(service.login(request));
  }
}
