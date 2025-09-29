package com.zeroHunger.demo.service;

import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

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
    return usuarioRepository.findByEmailAndSenha(email, senha);
  }
}
