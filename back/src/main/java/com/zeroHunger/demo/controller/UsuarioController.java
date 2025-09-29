package com.zeroHunger.demo.controller;

import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("/criar")
  public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
    try {
      Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
      return ResponseEntity.ok(novoUsuario);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(null);
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
  public ResponseEntity<Usuario> login(@RequestBody Usuario usuario) {
    Optional<Usuario> usuarioAutenticado = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());

    if (usuarioAutenticado.isPresent()) {
      return ResponseEntity.ok(usuarioAutenticado.get());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Retorna 401 se não for autenticado
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
