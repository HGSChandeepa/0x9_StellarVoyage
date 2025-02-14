package com.stellervoyage.backend.controller;

import com.stellervoyage.backend.dto.user.LoginRequest;
import com.stellervoyage.backend.dto.user.LoginResponse;
import com.stellervoyage.backend.dto.user.RegistrationRequest;
import com.stellervoyage.backend.dto.user.VerificationRequest;
import com.stellervoyage.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody @Valid RegistrationRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<LoginResponse> verifyUser(VerificationRequest request) {
        return ResponseEntity.ok(service.verifyEmail(request));
    }
}
