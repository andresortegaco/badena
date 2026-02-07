package com.badena.marketplace.dto;

public class ImagenDetalleDTO {
    private String urlImagen;
    private boolean esPortada;

    public ImagenDetalleDTO() {}

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public boolean isEsPortada() { return esPortada; }
    public void setEsPortada(boolean esPortada) { this.esPortada = esPortada; }
}