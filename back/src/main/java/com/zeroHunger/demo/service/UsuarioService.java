package com.zeroHunger.demo.service;

import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Usuario salvarUsuario(Usuario usuario) {
    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new RuntimeException("Email ja cadastrado!");
    }
    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> buscarUsuarioPorId(Long id) {
    return usuarioRepository.findById(id);
  }

  public Optional<Usuario> buscarUsuarioPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  public Optional<Usuario> autenticar(String email, String senha) {
    System.out.println("UsuarioService.autenticar - Email: " + email);
    System.out.println("UsuarioService.autenticar - Senha recebida: " + senha);
    
    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
    if (usuarioOpt.isPresent()) {
      Usuario usuario = usuarioOpt.get();
      System.out.println("UsuarioService.autenticar - Usuário encontrado: " + usuario.getEmail());
      System.out.println("UsuarioService.autenticar - Senha no banco: " + usuario.getSenha());
      
      boolean senhaMatch = passwordEncoder.matches(senha, usuario.getSenha());
      System.out.println("UsuarioService.autenticar - Senha confere: " + senhaMatch);
      
      if (senhaMatch) {
        return Optional.of(usuario);
      }
    } else {
      System.out.println("UsuarioService.autenticar - Usuário não encontrado para email: " + email);
    }
    return Optional.empty();
  }
}
