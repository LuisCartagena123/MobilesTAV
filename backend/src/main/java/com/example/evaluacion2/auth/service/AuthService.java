package com.example.evaluacion2.auth.service;

import com.example.evaluacion2.auth.dto.AuthResponse;
import com.example.evaluacion2.auth.jwt.JwtProvider;
import com.example.evaluacion2.usuarios.dto.UsuarioDTO;
import com.example.evaluacion2.usuarios.entity.UsuarioEntity;
import com.example.evaluacion2.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse registro(String email, String username, String nombre, String password) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya existe");
        }
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El username ya existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setIsAdmin(false);
        
        usuarioRepository.save(usuario);
        
        String token = jwtProvider.generateToken(email);
        return new AuthResponse(token, email, username, nombre, false);
    }

    public AuthResponse login(String identifier, String password) {
        UsuarioEntity usuario = usuarioRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtProvider.generateToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getEmail(), usuario.getUsername(), usuario.getNombre(), usuario.getIsAdmin());
    }
}
