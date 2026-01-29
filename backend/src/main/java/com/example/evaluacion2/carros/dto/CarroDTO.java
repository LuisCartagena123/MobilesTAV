package com.example.evaluacion2.carros.dto;

import com.example.evaluacion2.libros.dto.LibroDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarroDTO {
    private Integer id;
    private String usuarioEmail;
    private LibroDTO libro;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
