package com.zeroHunger.demo.controller;

import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/criar")
  public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
    try {
      // Criptografar senha
      usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
      
      // Definir tipo padrão se não informado
      if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
        usuario.setTipo("COMPRADOR");
      }

      // Definir data de nascimento padrão se não informada
      if (usuario.getDataNascimento() == null) {
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
      }

      Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
      return ResponseEntity.ok(novoUsuario);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("Erro ao criar usuário: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
    Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
    if (usuario.isPresent()) {
      return ResponseEntity.ok(usuario.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Usuario usuario) {
    try {
      // Debug: verificar se os dados estão chegando
      System.out.println("Tentativa de login - Email: " + usuario.getEmail());
      System.out.println("Tentativa de login - Senha: " + usuario.getSenha());
      
      Optional<Usuario> usuarioAutenticado = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());

      if (usuarioAutenticado.isPresent()) {
        System.out.println("Login bem-sucedido para: " + usuario.getEmail());
        return ResponseEntity.ok(usuarioAutenticado.get());
      } else {
        System.out.println("Falha na autenticação para: " + usuario.getEmail());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
      }
    } catch (Exception e) {
      System.out.println("Erro no login: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
    }
  }

  @GetMapping("/email")
  public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
    Optional<Usuario> usuario = usuarioService.buscarUsuarioPorEmail(email);
    if (usuario.isPresent()) {
      return ResponseEntity.ok(usuario.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
