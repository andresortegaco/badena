package com.badena.marketplace.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imagenes_producto")
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnore
    private Producto producto;

    @Column(name = "url_imagen", nullable = false, length = 512)
    private String urlImagen;

    private int orden = 1;

    @Column(name = "es_portada")
    private boolean esPortada = false;

    public ImagenProducto() { }

  // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public boolean isEsPortada() { return esPortada; }
    public void setEsPortada(boolean esPortada) { this.esPortada = esPortada; }
}