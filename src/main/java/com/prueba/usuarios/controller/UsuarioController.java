package com.prueba.usuarios.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.usuarios.dto.ApiResponse;
import com.prueba.usuarios.dto.UsuarioRequest;
import com.prueba.usuarios.dto.UsuarioResponse;
import com.prueba.usuarios.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.crearUsuario(request);
        ApiResponse<UsuarioResponse> respuesta = new ApiResponse<>("El usuario se creó exitosamente", usuario);
        return ResponseEntity.created(null).body(respuesta);
    }
}
