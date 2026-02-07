package com.badena.marketplace.dto;
public class LoginDTO {
    private String email;
    private String password;
    private String tipoUsuario;

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTipoUsuario() {return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) {this.tipoUsuario = tipoUsuario;}
    
}