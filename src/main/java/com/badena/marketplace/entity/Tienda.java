package com.badena.marketplace.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tiendas")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "nombre_publico", nullable = false, length = 150)
    private String nombrePublico;

    @Column(unique = true, nullable = false, length = 150)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String contacto;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "logo_url")
    private String logoUrl;

    private boolean activa = true;

    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Marca> marcas;
 
    public Tienda() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNombrePublico() { return nombrePublico; }
    public void setNombrePublico(String nombrePublico) { this.nombrePublico = nombrePublico; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public List<Marca> getMarcas() { return marcas; }
    public void setMarcas(List<Marca> marcas) { this.marcas = marcas; }
}