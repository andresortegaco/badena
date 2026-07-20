package com.badena.marketplace.dto;

public class CategoriaRegistroDTO {
    
    private String nombre;
    private Long idPadre; // Será null si es una categoría principal

    // Getters y Setters
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public Long getIdPadre() { 
        return idPadre; 
    }
    
    public void setIdPadre(Long idPadre) { 
        this.idPadre = idPadre; 
    }
}