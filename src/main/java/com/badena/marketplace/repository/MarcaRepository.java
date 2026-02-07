package com.badena.marketplace.repository;

import com.badena.marketplace.entity.Marca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    
    // Metodo para buscar solo las marcas vinculadas al ID de una tienda
    List<Marca> findByTiendaId(Long tiendaId);
    

   
}