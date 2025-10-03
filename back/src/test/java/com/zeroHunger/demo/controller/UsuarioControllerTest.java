package com.zeroHunger.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayName("Testes de Integração para UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setSenha("minhasenha");
        usuario.setNome("Teste Usuario");
        usuario.setTipo("COMPRADOR");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setTelefone("11999999999");
        usuario.setEndereco("Rua Teste, 123");
    }

    @Test
    @DisplayName("POST /api/usuarios/criar - Deve criar usuário com sucesso")
    @WithMockUser
    void deveCriarUsuarioComSucesso() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioService.salvarUsuario(any(Usuario.class))).thenReturn(usuario);

        // When & Then
        mockMvc.perform(post("/api/usuarios/criar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.nome").value("Teste Usuario"));

        verify(passwordEncoder).encode("minhasenha");
        verify(usuarioService).salvarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("POST /api/usuarios/criar - Deve retornar erro quando email já existe")
    @WithMockUser
    void deveRetornarErroQuandoEmailJaExiste() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioService.salvarUsuario(any(Usuario.class)))
                .thenThrow(new RuntimeException("Email ja cadastrado!"));

        // When & Then
        mockMvc.perform(post("/api/usuarios/criar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao criar usuário: Email ja cadastrado!"));

        verify(usuarioService).salvarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Deve buscar usuário por ID com sucesso")
    @WithMockUser
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        // Given
        Long id = 1L;
        when(usuarioService.buscarUsuarioPorId(id)).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.nome").value("Teste Usuario"));

        verify(usuarioService).buscarUsuarioPorId(id);
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Deve retornar 404 quando usuário não existe")
    @WithMockUser
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {
        // Given
        Long id = 999L;
        when(usuarioService.buscarUsuarioPorId(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarUsuarioPorId(id);
    }

    @Test
    @DisplayName("POST /api/usuarios/login - Deve fazer login com sucesso")
    @WithMockUser
    void deveFazerLoginComSucesso() throws Exception {
        // Given
        Usuario loginRequest = new Usuario();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setSenha("minhasenha");

        when(usuarioService.autenticar("teste@email.com", "minhasenha"))
                .thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(post("/api/usuarios/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(usuarioService).autenticar("teste@email.com", "minhasenha");
    }

    @Test
    @DisplayName("POST /api/usuarios/login - Deve retornar 401 para credenciais inválidas")
    @WithMockUser
    void deveRetornar401ParaCredenciaisInvalidas() throws Exception {
        // Given
        Usuario loginRequest = new Usuario();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setSenha("senhaerrada");

        when(usuarioService.autenticar("teste@email.com", "senhaerrada"))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/usuarios/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas"));

        verify(usuarioService).autenticar("teste@email.com", "senhaerrada");
    }

    @Test
    @DisplayName("GET /api/usuarios/email - Deve buscar usuário por email com sucesso")
    @WithMockUser
    void deveBuscarUsuarioPorEmailComSucesso() throws Exception {
        // Given
        String email = "teste@email.com";
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/api/usuarios/email")
                .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(usuarioService).buscarUsuarioPorEmail(email);
    }

    @Test
    @DisplayName("GET /api/usuarios/email - Deve retornar 404 quando email não existe")
    @WithMockUser
    void deveRetornar404QuandoEmailNaoExiste() throws Exception {
        // Given
        String email = "inexistente@email.com";
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/usuarios/email")
                .param("email", email))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarUsuarioPorEmail(email);
    }
}
