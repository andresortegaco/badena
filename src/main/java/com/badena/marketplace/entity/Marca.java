package com.badena.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonIgnore
    private Tienda tienda;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(unique = true, length = 100)
    private String slug;

    // NUEVO CAMPO PARA GUARDAR EL NOMBRE DEL ARCHIVO DE LA FOTO/LOGO
    @Column(name = "logo", length = 255)
    private String logo;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Producto> productos;

    public Marca() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tienda getTienda() { return tienda; }
    public void setTienda(Tienda tienda) { this.tienda = tienda; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}