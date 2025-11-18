package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.AuthService;
import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.user.dto.LoginResponseDTO;
import com.sprint.mission.discodeit.user.dto.LoginTester;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginTester> login(@RequestBody @Valid LoginRequestDTO request) {
    LoginTester loginResponse = authService.login(request);
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    return ResponseEntity.ok().build();
  }
}
