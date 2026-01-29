package com.example.evaluacion2.usuarios.service;

import com.example.evaluacion2.usuarios.dto.UsuarioDTO;
import com.example.evaluacion2.usuarios.entity.UsuarioEntity;
import com.example.evaluacion2.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
    }

    public UsuarioDTO hacerAdmin(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setIsAdmin(true);
        usuarioRepository.save(usuario);
        return convertToDTO(usuario);
    }

    public UsuarioDTO quitarAdmin(String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setIsAdmin(false);
        usuarioRepository.save(usuario);
        return convertToDTO(usuario);
    }

    public void eliminarUsuario(String email) {
        usuarioRepository.deleteById(email);
    }

    private UsuarioDTO convertToDTO(UsuarioEntity usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        dto.setNombre(usuario.getNombre());
        dto.setIsAdmin(usuario.getIsAdmin());
        return dto;
    }
}
