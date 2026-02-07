package com.badena.marketplace.dto;

public class RegistroCompletoDTO {
    // Datos del Usuario
    private String nombre;
    private String email;
    private String password;
    private String tipo;

    // Datos de la Tienda
    private String nombreTienda;
    private String slugTienda;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNombreTienda() {
        return nombreTienda;
    }
    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
    public String getSlugTienda() {
        return slugTienda;
    }
    public void setSlugTienda(String slugTienda) {
        this.slugTienda = slugTienda;
    }

    

    
}