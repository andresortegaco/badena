package com.badena.marketplace.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación hacia la categoría padre (Subcategoría -> Categoría Principal)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_padre")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore // Se añadió aquí para evitar el bucle infinito al generar la respuesta en la API
    private Categoria padre;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    // Relación hacia las subcategorías (Categoría Principal -> Subcategorías)
    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL)
    // Se eliminó el @JsonIgnore de aquí para que el frontend reciba las subcategorías
    private List<Categoria> subcategorias;

    public Categoria() { }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Categoria getPadre() { return padre; }
    public void setPadre(Categoria padre) { this.padre = padre; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public List<Categoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<Categoria> subcategorias) { this.subcategorias = subcategorias; }
}