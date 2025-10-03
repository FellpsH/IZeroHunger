package com.zeroHunger.demo.repository;

import com.zeroHunger.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaEncriptada");
        usuario.setNome("Teste Usuario");
        usuario.setTipo("COMPRADOR");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setTelefone("11999999999");
        usuario.setEndereco("Rua Teste, 123");
    }

    @Test
    @DisplayName("Deve encontrar usuário por email")
    void deveEncontrarUsuarioPorEmail() {
        // Given
        entityManager.persistAndFlush(usuario);

        // When
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("teste@email.com");

        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("teste@email.com", usuarioEncontrado.get().getEmail());
        assertEquals("Teste Usuario", usuarioEncontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando email não existe")
    void deveRetornarOptionalVazioQuandoEmailNaoExiste() {
        // When
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("inexistente@email.com");

        // Then
        assertFalse(usuarioEncontrado.isPresent());
    }

    @Test
    @DisplayName("Deve verificar se email existe")
    void deveVerificarSeEmailExiste() {
        // Given
        entityManager.persistAndFlush(usuario);

        // When
        boolean emailExiste = usuarioRepository.existsByEmail("teste@email.com");
        boolean emailNaoExiste = usuarioRepository.existsByEmail("inexistente@email.com");

        // Then
        assertTrue(emailExiste);
        assertFalse(emailNaoExiste);
    }

    @Test
    @DisplayName("Deve encontrar usuário por email e senha")
    void deveEncontrarUsuarioPorEmailESenha() {
        // Given
        entityManager.persistAndFlush(usuario);

        // When
        Optional<Usuario> usuarioEncontrado = usuarioRepository
                .findByEmailAndSenha("teste@email.com", "senhaEncriptada");

        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("teste@email.com", usuarioEncontrado.get().getEmail());
        assertEquals("senhaEncriptada", usuarioEncontrado.get().getSenha());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para senha incorreta")
    void deveRetornarOptionalVazioParaSenhaIncorreta() {
        // Given
        entityManager.persistAndFlush(usuario);

        // When
        Optional<Usuario> usuarioEncontrado = usuarioRepository
                .findByEmailAndSenha("teste@email.com", "senhaErrada");

        // Then
        assertFalse(usuarioEncontrado.isPresent());
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void deveSalvarUsuarioComSucesso() {
        // When
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Then
        assertNotNull(usuarioSalvo.getId());
        assertEquals("teste@email.com", usuarioSalvo.getEmail());
        assertEquals("Teste Usuario", usuarioSalvo.getNome());
        assertEquals("COMPRADOR", usuarioSalvo.getTipo());

        // Verificar se foi persistido no banco
        Usuario usuarioNoBanco = entityManager.find(Usuario.class, usuarioSalvo.getId());
        assertNotNull(usuarioNoBanco);
        assertEquals("teste@email.com", usuarioNoBanco.getEmail());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        // Given
        Usuario usuarioSalvo = entityManager.persistAndFlush(usuario);
        Long id = usuarioSalvo.getId();

        // When
        usuarioRepository.deleteById(id);
        entityManager.flush();

        // Then
        Usuario usuarioNoBanco = entityManager.find(Usuario.class, id);
        assertNull(usuarioNoBanco);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Given
        Usuario usuarioSalvo = entityManager.persistAndFlush(usuario);
        
        // When
        usuarioSalvo.setNome("Nome Atualizado");
        usuarioSalvo.setTipo("VENDEDOR");
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioSalvo);
        entityManager.flush();

        // Then
        assertEquals("Nome Atualizado", usuarioAtualizado.getNome());
        assertEquals("VENDEDOR", usuarioAtualizado.getTipo());

        // Verificar no banco
        Usuario usuarioNoBanco = entityManager.find(Usuario.class, usuarioSalvo.getId());
        assertEquals("Nome Atualizado", usuarioNoBanco.getNome());
        assertEquals("VENDEDOR", usuarioNoBanco.getTipo());
    }
}
