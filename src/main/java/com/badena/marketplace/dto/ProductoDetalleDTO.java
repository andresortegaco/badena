package com.badena.marketplace.dto;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class ProductoDetalleDTO {
    private String nombre;
    private String codigoUniversal;
    private String nombreMarca;
    private String nombreTienda;
    private String categoria;
    private String fotoPortada;
    private String descripcionTecnica;
    private String descripcionMarketing;
    private String contenidoNeto;
    private String unidadMedida;
    
    private BigDecimal pesoKg;
    private BigDecimal largoCm;
    private BigDecimal anchoCm;
    private BigDecimal altoCm;
    
    private List<ImagenDetalleDTO> imagenes = new ArrayList<>();

    public ProductoDetalleDTO() {}

    // Getters y Setters

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public BigDecimal getLargoCm() { return largoCm; }
    public void setLargoCm(BigDecimal largoCm) { this.largoCm = largoCm; }

    public BigDecimal getAnchoCm() { return anchoCm; }
    public void setAnchoCm(BigDecimal anchoCm) { this.anchoCm = anchoCm; }

    public BigDecimal getAltoCm() { return altoCm; }
    public void setAltoCm(BigDecimal altoCm) { this.altoCm = altoCm; }

    public List<ImagenDetalleDTO> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenDetalleDTO> imagenes) { this.imagenes = imagenes; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigoUniversal() { return codigoUniversal; }
    public void setCodigoUniversal(String codigoUniversal) { this.codigoUniversal = codigoUniversal; }

    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) { this.nombreMarca = nombreMarca; }

    public String getNombreTienda() { return nombreTienda; }
    public void setNombreTienda(String nombreTienda) { this.nombreTienda = nombreTienda; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFotoPortada() { return fotoPortada; }
    public void setFotoPortada(String fotoPortada) { this.fotoPortada = fotoPortada; }

    public String getDescripcionTecnica() { return descripcionTecnica; }
    public void setDescripcionTecnica(String descripcionTecnica) { this.descripcionTecnica = descripcionTecnica; }

    public String getDescripcionMarketing() { return descripcionMarketing; }
    public void setDescripcionMarketing(String descripcionMarketing) { this.descripcionMarketing = descripcionMarketing; }

    public String getContenidoNeto() { return contenidoNeto; }
    public void setContenidoNeto(String contenidoNeto) { this.contenidoNeto = contenidoNeto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
}

