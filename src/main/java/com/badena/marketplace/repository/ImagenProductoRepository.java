package com.badena.marketplace.repository;

import com.badena.marketplace.entity.ImagenProducto;
import com.badena.marketplace.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {
    void deleteByProducto(Producto producto);
}

