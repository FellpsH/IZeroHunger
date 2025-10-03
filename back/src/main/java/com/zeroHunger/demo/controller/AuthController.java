package com.zeroHunger.demo.controller;

import com.zeroHunger.demo.DTO.AuthResponse;
import com.zeroHunger.demo.DTO.LoginRequest;
import com.zeroHunger.demo.model.Usuario;
import com.zeroHunger.demo.repository.UsuarioRepository;
import com.zeroHunger.demo.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar usuário
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getSenha()
                )
            );

            // Carregar detalhes do usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            
            // Buscar usuário completo no banco
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            Usuario usuario = usuarioOpt.get();

            // Gerar token JWT
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", usuario.getId());
            extraClaims.put("tipo", usuario.getTipo());
            
            String token = jwtUtil.generateToken(userDetails, extraClaims);

            // Criar resposta
            AuthResponse authResponse = new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getTipo(),
                usuario.getEndereco(),
                usuario.getId()
            );

            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario) {
        try {
            // Verificar se email já existe
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().body("Email já está em uso");
            }

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

            // Salvar usuário
            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            // Gerar token para o novo usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
            
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", usuarioSalvo.getId());
            extraClaims.put("tipo", usuarioSalvo.getTipo());
            
            String token = jwtUtil.generateToken(userDetails, extraClaims);

            // Criar resposta
            AuthResponse authResponse = new AuthResponse(
                token,
                usuarioSalvo.getEmail(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getTipo(),
                usuarioSalvo.getEndereco(),
                usuarioSalvo.getId()
            );

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no registro: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (jwtUtil.isTokenValid(token)) {
                    String email = jwtUtil.extractUsername(token);
                    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                    
                    if (usuarioOpt.isPresent()) {
                        Usuario usuario = usuarioOpt.get();
                        Map<String, Object> response = new HashMap<>();
                        response.put("valid", true);
                        response.put("user", Map.of(
                            "id", usuario.getId(),
                            "email", usuario.getEmail(),
                            "nome", usuario.getNome(),
                            "tipo", usuario.getTipo(),
                            "endereco", usuario.getEndereco()
                        ));
                        return ResponseEntity.ok(response);
                    }
                }
            }
            
            return ResponseEntity.badRequest().body(Map.of("valid", false));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", e.getMessage()));
        }
    }
}
