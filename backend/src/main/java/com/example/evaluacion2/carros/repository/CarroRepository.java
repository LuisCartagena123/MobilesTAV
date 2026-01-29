package com.example.evaluacion2.carros.repository;

import com.example.evaluacion2.carros.entity.CarroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<CarroEntity, Integer> {
    List<CarroEntity> findByUsuarioEmail(String email);
    Optional<CarroEntity> findByUsuarioEmailAndLibroId(String email, Integer libroId);
    void deleteByLibroId(Integer libroId);
}
