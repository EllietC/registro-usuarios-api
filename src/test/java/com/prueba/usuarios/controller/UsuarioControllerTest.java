package com.prueba.usuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.usuarios.dto.TelefonoRequest;
import com.prueba.usuarios.dto.UsuarioRequest;
import com.prueba.usuarios.dto.UsuarioResponse;
import com.prueba.usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@Import(UsuarioControllerTest.Config.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UsuarioService usuarioService;

	@TestConfiguration
	static class Config {
		@Bean
		public UsuarioService usuarioService() {
			return Mockito.mock(UsuarioService.class);
		}
	}

	@Test
	void registrarUsuarioCreacionExitosaRetornaUsuario() throws Exception {
		UsuarioResponse usuario = new UsuarioResponse();
		usuario.setId(UUID.randomUUID());
		usuario.setToken("token-ejemplo");
		usuario.setName("Juan Rodriguez");
		usuario.setEmail("juan@rodriguez.com");
		usuario.setCreated(LocalDateTime.now());
		usuario.setModified(LocalDateTime.now());
		usuario.setLastLogin(LocalDateTime.now());
		usuario.setActive(true);

		when(usuarioService.crearUsuario(any(UsuarioRequest.class))).thenReturn(usuario);

		UsuarioRequest request = new UsuarioRequest();
		request.setName("Juan Rodriguez");
		request.setEmail("juan@rodriguez.org");
		request.setPassword("Password12");
		request.setPhones(List.of(
				new TelefonoRequest("123456789", "1", "56"),
				new TelefonoRequest("987654321", "1", "56")));

		mockMvc.perform(post("/api/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mensaje").value("El usuario se creó exitosamente"))
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.id").isNotEmpty())
				.andExpect(jsonPath("$.data.token").value("token-ejemplo"))
				.andExpect(jsonPath("$.data.created").isNotEmpty())
				.andExpect(jsonPath("$.data.modified").isNotEmpty())
				.andExpect(jsonPath("$.data.lastLogin").isNotEmpty())
				.andExpect(jsonPath("$.data.active").value(true));
	}

	@Test
	void registrarUsuarioEmailDuplicadoRetornaError409() throws Exception {
		when(usuarioService.crearUsuario(any(UsuarioRequest.class)))
				.thenThrow(new ResponseStatusException(HttpStatus.CONFLICT,
						"El correo ya fue registrado"));

		UsuarioRequest request = new UsuarioRequest();
		request.setName("Juan Rodriguez");
		request.setEmail("juan@rodriguez.org");
		request.setPassword("Password12");
		request.setPhones(List.of(
				new TelefonoRequest("123456789", "1", "56"),
				new TelefonoRequest("987654321", "1", "56")));

		mockMvc.perform(post("/api/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("El correo ya fue registrado"));
	}

	@Test
	void registrarUsuarioDatosInvalidosRetornaError400() throws Exception {
		UsuarioRequest request = new UsuarioRequest();
		request.setName("");
		request.setEmail("juan@rodriguez.org");
		request.setPassword("Password12");
		request.setPhones(List.of(
				new TelefonoRequest("123456789", "1", "56"),
				new TelefonoRequest("987654321", "1", "56")));
		mockMvc.perform(post("/api/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("El campo name es obligatorio"));
	}

	@Test
	void registrarUsuarioRequestNuloRetornaError400() throws Exception {
		mockMvc.perform(post("/api/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void registrarUsuarioErrorInternoRetornaError500() throws Exception {
		when(usuarioService.crearUsuario(any(UsuarioRequest.class)))
				.thenThrow(new RuntimeException("Error interno del servidor"));

		UsuarioRequest request = new UsuarioRequest();
		request.setName("Juan Rodriguez");
		request.setEmail("juan@rodriguez.org");
		request.setPassword("Password12");
		request.setPhones(List.of(
				new TelefonoRequest("123456789", "1", "56"),
				new TelefonoRequest("987654321", "1", "56")));
		mockMvc.perform(post("/api/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.mensaje").value("Error interno del servidor"));
	}
}
