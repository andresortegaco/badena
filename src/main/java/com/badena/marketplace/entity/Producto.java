package com.badena.marketplace.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacion con Tienda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tienda", nullable = false)
    @JsonIgnoreProperties({"usuario", "marcas", "productos", "hibernateLazyInitializer", "handler"})
    private Tienda tienda; 

    // Relacion con Marca (Exclusiva de una Tienda)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    @JsonIgnoreProperties({"tienda", "productos", "hibernateLazyInitializer", "handler"})
    private Marca marca;

    // Relacion con Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "codigo_universal", unique = true, nullable = false, length = 50)
    private String codigoUniversal;

    @Column(name = "sku_vendedor", unique = false, nullable = false, length = 100)
    private String skuVendedor;

    @Column(name = "sku_interno", length = 100)
    private String skuInterno;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(name = "descripcion_marketing", columnDefinition = "TEXT")
    private String descripcionMarketing;

    @Column(name = "descripcion_tecnica", columnDefinition = "TEXT")
    private String descripcionTecnica;

    @Column(name = "peso_kg", precision = 10, scale = 3)
    private BigDecimal pesoKg;

    @Column(name = "alto_cm", precision = 10, scale = 2)
    private BigDecimal altoCm;

    @Column(name = "ancho_cm", precision = 10, scale = 2)
    private BigDecimal anchoCm;

    @Column(name = "largo_cm", precision = 10, scale = 2)
    private BigDecimal largoCm;

    @Column(name = "contenido_neto", length = 50)
    private String contenidoNeto;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "material_principal", length = 100)
    private String materialPrincipal;

    @Column(name = "pais_origen", length = 50)
    private String paisOrigen;

    @Column(name = "ultima_sincronizacion")
    private LocalDateTime ultimaSincronizacion;

    private boolean visible = true;


    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenProducto> imagenes;


    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AtributoEspecifico> atributos;

    @ManyToMany
    @JoinTable(
        name = "producto_etiqueta",
        joinColumns = @JoinColumn(name = "id_producto"),
        inverseJoinColumns = @JoinColumn(name = "id_etiqueta")
    )
    private List<Etiqueta> etiquetas;


    public Producto() {
        this.imagenes = new ArrayList<>();
        this.atributos = new ArrayList<>();
        this.etiquetas = new ArrayList<>();
        this.ultimaSincronizacion = LocalDateTime.now();
    }


    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.ultimaSincronizacion = LocalDateTime.now();
    }

// Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getCodigoUniversal() { return codigoUniversal; }
    public void setCodigoUniversal(String codigoUniversal) { this.codigoUniversal = codigoUniversal; }

    public String getSkuVendedor() { return skuVendedor; }
    public void setSkuVendedor(String skuVendedor) { this.skuVendedor = skuVendedor; }

    public String getSkuInterno() { return skuInterno; }
    public void setSkuInterno(String skuInterno) { this.skuInterno = skuInterno; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescripcionMarketing() { return descripcionMarketing; }
    public void setDescripcionMarketing(String descripcionMarketing) { this.descripcionMarketing = descripcionMarketing; }

    public String getDescripcionTecnica() { return descripcionTecnica; }
    public void setDescripcionTecnica(String descripcionTecnica) { this.descripcionTecnica = descripcionTecnica; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public BigDecimal getAltoCm() { return altoCm; }
    public void setAltoCm(BigDecimal altoCm) { this.altoCm = altoCm; }

    public BigDecimal getAnchoCm() { return anchoCm; }
    public void setAnchoCm(BigDecimal anchoCm) { this.anchoCm = anchoCm; }

    public BigDecimal getLargoCm() { return largoCm; }
    public void setLargoCm(BigDecimal largoCm) { this.largoCm = largoCm; }

    public String getContenidoNeto() { return contenidoNeto; }
    public void setContenidoNeto(String contenidoNeto) { this.contenidoNeto = contenidoNeto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getMaterialPrincipal() { return materialPrincipal; }
    public void setMaterialPrincipal(String materialPrincipal) { this.materialPrincipal = materialPrincipal; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public LocalDateTime getUltimaSincronizacion() { return ultimaSincronizacion; }
    public void setUltimaSincronizacion(LocalDateTime ultimaSincronizacion) { this.ultimaSincronizacion = ultimaSincronizacion; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public List<ImagenProducto> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenProducto> imagenes) { this.imagenes = imagenes; }

    public List<AtributoEspecifico> getAtributos() { return atributos; }
    public void setAtributos(List<AtributoEspecifico> atributos) { this.atributos = atributos; }

    public List<Etiqueta> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<Etiqueta> etiquetas) { this.etiquetas = etiquetas; }

    public Tienda getTienda() {return tienda; }
    public void setTienda(Tienda tienda) {this.tienda = tienda;}
    
    
    public void addImagen(ImagenProducto imagen) {
        this.imagenes.add(imagen);
        imagen.setProducto(this);
    }

    
}