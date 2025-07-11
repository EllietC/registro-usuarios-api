package com.prueba.usuarios.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.prueba.usuarios.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private UUID id;
    private String name;
    private String email;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.name = usuario.getName();
        this.email = usuario.getEmail();
        this.created = usuario.getCreated();
        this.modified = usuario.getModified();
        this.lastLogin = usuario.getLastLogin();
        this.token = usuario.getToken();
        this.isActive = usuario.isActive();
    }
}
