package com.example.evaluacion2.libros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {
    private Integer id;
    
    @NotBlank(message = "El título es requerido")
    private String titulo;
    
    @NotBlank(message = "El autor es requerido")
    private String autor;
    
    @NotNull(message = "Las páginas son requeridas")
    @Positive(message = "Las páginas deben ser positivas")
    private Integer paginas;
    
    private String descripcion;
    private String imagenUri;
    
    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser positivo")
    private Double precio;
}
