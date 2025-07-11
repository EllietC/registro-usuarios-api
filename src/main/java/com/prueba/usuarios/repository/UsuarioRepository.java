package com.prueba.usuarios.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.usuarios.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    boolean existsByEmail(String email);
}
