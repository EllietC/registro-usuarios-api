package com.prueba.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.prueba.usuarios.config.security.JwtUtil;
import com.prueba.usuarios.dto.TelefonoRequest;
import com.prueba.usuarios.dto.UsuarioRequest;
import com.prueba.usuarios.dto.UsuarioResponse;
import com.prueba.usuarios.entity.Usuario;
import com.prueba.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImp usuarioService;

    @Test
    void crearUsuarioEmailDuplicadoLanzaExcepcion() {
        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("test@dominio.com");
        request.setName("Test User");
        request.setPassword("Password123");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.crearUsuario(request);
        });

        assertEquals("El correo ya fue registrado", exception.getReason());
        verify(usuarioRepository, times(1)).existsByEmail(request.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuarioExitosoRetornaUsuarioPersistido() {
        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("nuevo@dominio.com");
        request.setName("Nuevo Usuario");
        request.setPassword("Password123");
        request.setPhones(List.of(
                new TelefonoRequest("123456789", "1", "56"),
                new TelefonoRequest("987654321", "1", "56")));

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(jwtUtil.generarToken(anyString())).thenReturn("fake-jwt-token");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(java.util.UUID.randomUUID());
        usuarioGuardado.setEmail(request.getEmail());
        usuarioGuardado.setName(request.getName());
        usuarioGuardado.setPassword(request.getPassword());
        usuarioGuardado.setActive(true);
        usuarioGuardado.setCreated(LocalDateTime.now());
        usuarioGuardado.setModified(LocalDateTime.now());
        usuarioGuardado.setLastLogin(LocalDateTime.now());
        usuarioGuardado.setToken("fake-jwt-token");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioResponse resultado = usuarioService.crearUsuario(request);

        assertNotNull(resultado);
        assertEquals("nuevo@dominio.com", resultado.getEmail());
        assertEquals("Nuevo Usuario", resultado.getName());
        assertEquals("fake-jwt-token", resultado.getToken());

        verify(usuarioRepository, times(1)).existsByEmail("nuevo@dominio.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
