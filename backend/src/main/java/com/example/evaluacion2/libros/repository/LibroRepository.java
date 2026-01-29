package com.example.evaluacion2.libros.repository;

import com.example.evaluacion2.libros.entity.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<LibroEntity, Integer> {
}
