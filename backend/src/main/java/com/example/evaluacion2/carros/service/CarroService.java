package com.example.evaluacion2.carros.service;

import com.example.evaluacion2.carros.dto.AgregarCarroRequest;
import com.example.evaluacion2.carros.dto.CarroDTO;
import com.example.evaluacion2.carros.entity.CarroEntity;
import com.example.evaluacion2.carros.repository.CarroRepository;
import com.example.evaluacion2.libros.dto.LibroDTO;
import com.example.evaluacion2.libros.entity.LibroEntity;
import com.example.evaluacion2.libros.repository.LibroRepository;
import com.example.evaluacion2.usuarios.entity.UsuarioEntity;
import com.example.evaluacion2.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {
    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CarroDTO> obtenerCarroUsuario(String email) {
        return carroRepository.findByUsuarioEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarroDTO agregarAlCarro(String email, AgregarCarroRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LibroEntity libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        var carroExistente = carroRepository.findByUsuarioEmailAndLibroId(email, request.getLibroId());

        CarroEntity carro;
        if (carroExistente.isPresent()) {
            carro = carroExistente.get();
            carro.setCantidad(carro.getCantidad() + request.getCantidad());
        } else {
            carro = new CarroEntity();
            carro.setUsuario(usuario);
            carro.setLibro(libro);
            carro.setCantidad(request.getCantidad());
            carro.setPrecioUnitario(libro.getPrecio());
        }

        carroRepository.save(carro);
        return convertToDTO(carro);
    }

    public void removerDelCarro(Integer carroId, String email) {
        CarroEntity carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));

        if (!carro.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }

        carroRepository.deleteById(carroId);
    }

    public CarroDTO actualizarCantidad(Integer carroId, Integer cantidad, String email) {
        CarroEntity carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));

        if (!carro.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }

        if (cantidad <= 0) {
            carroRepository.deleteById(carroId);
            return null;
        }

        carro.setCantidad(cantidad);
        carroRepository.save(carro);
        return convertToDTO(carro);
    }

    public Double calcularTotal(String email) {
        return carroRepository.findByUsuarioEmail(email).stream()
                .mapToDouble(c -> c.getCantidad() * c.getPrecioUnitario())
                .sum();
    }

    private CarroDTO convertToDTO(CarroEntity carro) {
        LibroEntity libro = carro.getLibro();
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor());
        libroDTO.setPaginas(libro.getPaginas());
        libroDTO.setDescripcion(libro.getDescripcion());
        libroDTO.setImagenUri(libro.getImagenUri());
        libroDTO.setPrecio(libro.getPrecio());
        
        CarroDTO dto = new CarroDTO();
        dto.setId(carro.getId());
        dto.setUsuarioEmail(carro.getUsuario().getEmail());
        dto.setLibro(libroDTO);
        dto.setCantidad(carro.getCantidad());
        dto.setPrecioUnitario(carro.getPrecioUnitario());
        dto.setSubtotal(carro.getCantidad() * carro.getPrecioUnitario());
        return dto;
    }
}
