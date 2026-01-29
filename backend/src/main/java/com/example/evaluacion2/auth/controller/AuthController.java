package com.example.evaluacion2.auth.controller;

import com.example.evaluacion2.auth.dto.AuthRequest;
import com.example.evaluacion2.auth.dto.AuthResponse;
import com.example.evaluacion2.auth.dto.RegistroRequest;
import com.example.evaluacion2.auth.service.AuthService;
import com.example.evaluacion2.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<AuthResponse>> registro(@Valid @RequestBody RegistroRequest dto) {
        try {
            AuthResponse response = authService.registro(dto.getEmail(), dto.getUsername(), dto.getNombre(), dto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Usuario registrado exitosamente", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en registro", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest dto) {
        try {
            AuthResponse response = authService.login(dto.getIdentifier(), dto.getPassword());
            return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Error en login", e.getMessage()));
        }
    }
}
