package com.example.evaluacion2.carros.controller;

import com.example.evaluacion2.carros.dto.AgregarCarroRequest;
import com.example.evaluacion2.carros.dto.CarroDTO;
import com.example.evaluacion2.carros.service.CarroService;
import com.example.evaluacion2.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/carros")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://10.0.2.2:8080", "http://192.168.1.5:8080"})
public class CarroController {
    @Autowired
    private CarroService carroService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarroDTO>>> obtenerCarro(Principal principal) {
        String email = principal.getName();
        List<CarroDTO> carro = carroService.obtenerCarroUsuario(email);
        return ResponseEntity.ok(ApiResponse.ok("Carro obtenido", carro));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CarroDTO>> agregarAlCarro(
            Principal principal,
            @RequestBody AgregarCarroRequest request) {
        String email = principal.getName();
        CarroDTO carro = carroService.agregarAlCarro(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Item agregado al carro", carro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> removerDelCarro(
            @PathVariable Integer id,
            Principal principal) {
        String email = principal.getName();
        carroService.removerDelCarro(id, email);
        return ResponseEntity.ok(ApiResponse.ok("Item removido del carro", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarroDTO>> actualizarCantidad(
            @PathVariable Integer id,
            @RequestParam Integer cantidad,
            Principal principal) {
        String email = principal.getName();
        CarroDTO carro = carroService.actualizarCantidad(id, cantidad, email);
        return ResponseEntity.ok(ApiResponse.ok("Cantidad actualizada", carro));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> calcularTotal(Principal principal) {
        String email = principal.getName();
        Double total = carroService.calcularTotal(email);
        return ResponseEntity.ok(ApiResponse.ok("Total del carro", total));
    }
}
