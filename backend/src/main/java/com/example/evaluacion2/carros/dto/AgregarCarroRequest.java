package com.example.evaluacion2.carros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarCarroRequest {
    private Integer libroId;
    private Integer cantidad;
}
