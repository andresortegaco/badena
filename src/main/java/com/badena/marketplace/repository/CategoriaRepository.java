package com.badena.marketplace.repository;

import com.badena.marketplace.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Trae solo las categorías "Raíz" (las que no tienen un padre asignado)
    List<Categoria> findByPadreIsNull();

}