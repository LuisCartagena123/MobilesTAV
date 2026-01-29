package com.example.evaluacion2.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String email;
    private String username;
    private String nombre;
    private Boolean isAdmin;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UsuarioRegistroDTO {
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UsuarioLoginDTO {
    @NotBlank(message = "Email o usuario requerido")
    private String identifier;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
