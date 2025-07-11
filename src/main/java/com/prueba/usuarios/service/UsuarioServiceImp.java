package com.prueba.usuarios.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.prueba.usuarios.config.security.JwtUtil;
import com.prueba.usuarios.dto.TelefonoRequest;
import com.prueba.usuarios.dto.UsuarioRequest;
import com.prueba.usuarios.dto.UsuarioResponse;
import com.prueba.usuarios.entity.Telefono;
import com.prueba.usuarios.entity.Usuario;
import com.prueba.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioServiceImp implements UsuarioService {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImp(JwtUtil jwtUtil, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya fue registrado");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        List<Telefono> telefonos = request.getPhones().stream().map(this::convertirTelefono).toList();
        String token = jwtUtil.generarToken(request.getEmail());

        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .phones(telefonos)
                .token(token)
                .build();

        return new UsuarioResponse(usuarioRepository.save(usuario));
    }

    private Telefono convertirTelefono(TelefonoRequest t) {
        return Telefono.builder()
                .number(t.getNumber())
                .cityCode(t.getCitycode())
                .contryCode(t.getContrycode())
                .build();
    }
}
