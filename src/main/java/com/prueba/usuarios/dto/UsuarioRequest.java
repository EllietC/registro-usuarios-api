package com.prueba.usuarios.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.prueba.usuarios.validator.ValidPassword;

import lombok.Data;

@Data
public class UsuarioRequest {

    @NotBlank(message = "El campo name es obligatorio")
    private String name;

    @NotBlank(message = "El campo email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "El campo password es obligatorio")
    @ValidPassword
    private String password;

    private List<TelefonoRequest> phones;
}
