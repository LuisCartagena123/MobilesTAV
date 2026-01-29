package com.example.evaluacion2.carros.entity;

import com.example.evaluacion2.libros.entity.LibroEntity;
import com.example.evaluacion2.usuarios.entity.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "carros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_email", referencedColumnName = "email")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private LibroEntity libro;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;
}
