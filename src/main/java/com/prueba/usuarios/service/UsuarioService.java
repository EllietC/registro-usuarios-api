package com.prueba.usuarios.service;

import com.prueba.usuarios.dto.UsuarioRequest;
import com.prueba.usuarios.dto.UsuarioResponse;

public interface UsuarioService {
    /**
     * Crea un nuevo usuario a partir de la solicitud.
     *
     * @param request Datos del usuario a crear.
     * @return El usuario creado.
     */
    UsuarioResponse crearUsuario(UsuarioRequest request);
}
