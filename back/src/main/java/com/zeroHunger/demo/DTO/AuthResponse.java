package com.zeroHunger.demo.DTO;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private String email;
    private String nome;
    private String tipo;
    private String endereco;
    private Long id;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String token, String email, String nome, String tipo, String endereco, Long id) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.tipo = tipo;
        this.endereco = endereco;
        this.id = id;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
