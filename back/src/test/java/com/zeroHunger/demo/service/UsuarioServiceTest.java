package com.zeroHunger.demo.service;

import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.UsuarioRepository;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaEncriptada");
        usuario.setNome("Teste Usuario");
        usuario.setTipo("COMPRADOR");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setTelefone("11999999999");
        usuario.setEndereco("Rua Teste, 123");
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso quando email não existe")
    void deveSalvarUsuarioComSucesso() {
        // Given
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);

        // Then
        assertNotNull(usuarioSalvo);
        assertEquals(usuario.getEmail(), usuarioSalvo.getEmail());
        assertEquals(usuario.getNome(), usuarioSalvo.getNome());
        
        verify(usuarioRepository).existsByEmail(usuario.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.salvarUsuario(usuario));
        
        assertEquals("Email ja cadastrado!", exception.getMessage());
        verify(usuarioRepository).existsByEmail(usuario.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        // Given
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(id);

        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals(usuario.getId(), usuarioEncontrado.get().getId());
        assertEquals(usuario.getEmail(), usuarioEncontrado.get().getEmail());
        
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existe")
    void deveRetornarOptionalVazioQuandoUsuarioNaoExiste() {
        // Given
        Long id = 999L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(id);

        // Then
        assertFalse(usuarioEncontrado.isPresent());
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void deveBuscarUsuarioPorEmailComSucesso() {
        // Given
        String email = "teste@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);

        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals(usuario.getEmail(), usuarioEncontrado.get().getEmail());
        
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve autenticar usuário com credenciais válidas")
    void deveAutenticarUsuarioComCredenciaisValidas() {
        // Given
        String email = "teste@email.com";
        String senha = "minhasenha";
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        // When
        Optional<Usuario> usuarioAutenticado = usuarioService.autenticar(email, senha);

        // Then
        assertTrue(usuarioAutenticado.isPresent());
        assertEquals(usuario.getEmail(), usuarioAutenticado.get().getEmail());
        
        verify(usuarioRepository).findByEmail(email);
        verify(passwordEncoder).matches(senha, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve falhar na autenticação com senha inválida")
    void deveFalharNaAutenticacaoComSenhaInvalida() {
        // Given
        String email = "teste@email.com";
        String senhaInvalida = "senhaerrada";
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaInvalida, usuario.getSenha())).thenReturn(false);

        // When
        Optional<Usuario> usuarioAutenticado = usuarioService.autenticar(email, senhaInvalida);

        // Then
        assertFalse(usuarioAutenticado.isPresent());
        
        verify(usuarioRepository).findByEmail(email);
        verify(passwordEncoder).matches(senhaInvalida, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve falhar na autenticação com email inexistente")
    void deveFalharNaAutenticacaoComEmailInexistente() {
        // Given
        String emailInexistente = "inexistente@email.com";
        String senha = "qualquersenha";
        
        when(usuarioRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> usuarioAutenticado = usuarioService.autenticar(emailInexistente, senha);

        // Then
        assertFalse(usuarioAutenticado.isPresent());
        
        verify(usuarioRepository).findByEmail(emailInexistente);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
