package com.badena.marketplace.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "etiquetas")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String tag;
    
    @ManyToMany(mappedBy = "etiquetas")
    private List<Producto> productos;

    public Etiqueta() { }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}