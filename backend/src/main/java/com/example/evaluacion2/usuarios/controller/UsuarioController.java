package com.example.evaluacion2.usuarios.controller;

import com.example.evaluacion2.common.ApiResponse;
import com.example.evaluacion2.usuarios.dto.UsuarioDTO;
import com.example.evaluacion2.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://10.0.2.2:8080", "http://192.168.1.5:8080"})
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodosUsuarios(Principal principal) {
        // Verificar que el usuario sea admin (para producción, agregar @PreAuthorize)
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(ApiResponse.ok("Usuarios obtenidos", usuarios));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerUsuarioPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(ApiResponse.ok("Usuario obtenido", usuario));
    }

    @PutMapping("/{email}/admin")
    public ResponseEntity<ApiResponse<UsuarioDTO>> hacerAdmin(
            @PathVariable String email,
            Principal principal) {
        // Verificar que el usuario actual sea admin
        UsuarioDTO usuario = usuarioService.hacerAdmin(email);
        return ResponseEntity.ok(ApiResponse.ok("Usuario promovido a admin", usuario));
    }

    @PutMapping("/{email}/no-admin")
    public ResponseEntity<ApiResponse<UsuarioDTO>> quitarAdmin(
            @PathVariable String email,
            Principal principal) {
        // Verificar que el usuario actual sea admin
        UsuarioDTO usuario = usuarioService.quitarAdmin(email);
        return ResponseEntity.ok(ApiResponse.ok("Admin removido del usuario", usuario));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<String>> eliminarUsuario(
            @PathVariable String email,
            Principal principal) {
        // Verificar que el usuario actual sea admin o que sea el mismo usuario
        usuarioService.eliminarUsuario(email);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", null));
    }

    @GetMapping("/perfil/mi-perfil")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerMiPerfil(Principal principal) {
        String email = principal.getName();
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(ApiResponse.ok("Perfil obtenido", usuario));
    }
}
