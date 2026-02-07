package com.badena.marketplace.repository;

import com.badena.marketplace.entity.Tienda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    // Buscar tienda de 'id_usuario'
    Optional<Tienda> findByUsuarioId(Long idUsuario);
    
}