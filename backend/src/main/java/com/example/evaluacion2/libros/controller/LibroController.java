package com.example.evaluacion2.libros.controller;

import com.example.evaluacion2.common.ApiResponse;
import com.example.evaluacion2.carros.repository.CarroRepository;
import com.example.evaluacion2.libros.dto.LibroDTO;
import com.example.evaluacion2.libros.entity.LibroEntity;
import com.example.evaluacion2.libros.repository.LibroRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroRepository libroRepository;
    private final CarroRepository carroRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LibroDTO>>> obtenerLibros() {
        List<LibroDTO> libros = libroRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Libros obtenidos", libros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> obtenerLibro(@PathVariable Integer id) {
        return libroRepository.findById(id)
                .map(libro -> ResponseEntity.ok(ApiResponse.ok("Libro obtenido", entityToDto(libro))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LibroDTO>> crearLibro(@Valid @RequestBody LibroDTO dto) {
        LibroEntity libro = new LibroEntity();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setPaginas(dto.getPaginas());
        libro.setDescripcion(dto.getDescripcion());
        libro.setImagenUri(dto.getImagenUri());
        libro.setPrecio(dto.getPrecio());
        
        LibroEntity saved = libroRepository.save(libro);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Libro creado", entityToDto(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarLibro(@PathVariable Integer id, @Valid @RequestBody LibroDTO dto) {
        return libroRepository.findById(id)
                .map(libro -> {
                    libro.setTitulo(dto.getTitulo());
                    libro.setAutor(dto.getAutor());
                    libro.setPaginas(dto.getPaginas());
                    libro.setDescripcion(dto.getDescripcion());
                    libro.setImagenUri(dto.getImagenUri());
                    libro.setPrecio(dto.getPrecio());
                    LibroEntity updated = libroRepository.save(libro);
                    return ResponseEntity.ok(ApiResponse.ok("Libro actualizado", entityToDto(updated)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> eliminarLibro(@PathVariable Integer id) {
        if (libroRepository.existsById(id)) {
            carroRepository.deleteByLibroId(id);
            libroRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.ok("Libro eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    private LibroDTO entityToDto(LibroEntity entity) {
        return new LibroDTO(entity.getId(), entity.getTitulo(), entity.getAutor(), 
                entity.getPaginas(), entity.getDescripcion(), entity.getImagenUri(), entity.getPrecio());
    }
}
