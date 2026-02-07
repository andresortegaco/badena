package com.badena.marketplace.dto;

import java.math.BigDecimal;

public class ProductoRegistroDTO {
    private String nombre;
    private Long idMarca;      
    private Long idCategoria;  
    private String skuVendedor;
    private String codigoUniversal;
    private String descripcionMarketing;
    private String descripcionTecnica;
    private Long idTienda;
      
 
    private String contenidoNeto;
    private String unidadMedida;
    
 
    private BigDecimal pesoKg;
    private BigDecimal largoCm;
    private BigDecimal anchoCm;
    private BigDecimal altoCm;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    public String getSkuVendedor() { return skuVendedor; }
    public void setSkuVendedor(String skuVendedor) { this.skuVendedor = skuVendedor; }
    public String getCodigoUniversal() { return codigoUniversal; }
    public void setCodigoUniversal(String codigoUniversal) { this.codigoUniversal = codigoUniversal; }
    public String getDescripcionMarketing() { return descripcionMarketing; }
    public void setDescripcionMarketing(String descripcionMarketing) { this.descripcionMarketing = descripcionMarketing; }
    public String getDescripcionTecnica() { return descripcionTecnica; }
    public void setDescripcionTecnica(String descripcionTecnica) { this.descripcionTecnica = descripcionTecnica; }
    public String getContenidoNeto() { return contenidoNeto; }
    public void setContenidoNeto(String contenidoNeto) { this.contenidoNeto = contenidoNeto; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }
    public BigDecimal getLargoCm() { return largoCm; }
    public void setLargoCm(BigDecimal largoCm) { this.largoCm = largoCm; }
    public BigDecimal getAnchoCm() { return anchoCm; }
    public void setAnchoCm(BigDecimal anchoCm) { this.anchoCm = anchoCm; }
    public BigDecimal getAltoCm() { return altoCm; }
    public void setAltoCm(BigDecimal altoCm) { this.altoCm = altoCm; }
    public Long getIdTienda() {return idTienda;}
    public void setIdTienda(Long idTienda) {this.idTienda = idTienda;}
    
}