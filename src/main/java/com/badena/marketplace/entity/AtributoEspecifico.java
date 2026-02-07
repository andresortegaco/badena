package com.badena.marketplace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "atributos_especificos")
public class AtributoEspecifico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "nombre_atributo", nullable = false, length = 100)
    private String nombreAtributo;

    @Column(nullable = false)
    private String valor;

    public AtributoEspecifico() { }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getNombreAtributo() { return nombreAtributo; }
    public void setNombreAtributo(String nombreAtributo) { this.nombreAtributo = nombreAtributo; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}