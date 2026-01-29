package com.example.evaluacion2.libros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(nullable = false)
    private String autor;
    
    @Column(nullable = false)
    private Integer paginas;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private String imagenUri;
    
    @Column(nullable = false)
    private Double precio;
}
