package com.example.evaluacion2.usuarios.repository;

import com.example.evaluacion2.usuarios.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findByUsername(String username);
    Optional<UsuarioEntity> findByEmailOrUsername(String email, String username);
}
