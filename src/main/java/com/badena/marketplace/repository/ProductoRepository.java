package com.badena.marketplace.repository;

import com.badena.marketplace.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    long countByMarca_Tienda_Id(Long idTienda);

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.marca.tienda.id = :idTienda")
    long contarProductosDeMiTienda(@Param("idTienda") Long idTienda);



    List<Producto> findByTiendaId(Long tiendaId);

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);




    List<Producto> findByNombreProductoContainingIgnoreCaseOrCodigoUniversalContainingIgnoreCase(
        String nombre, String codigoUniversal);

        Optional<Producto> findByCodigoUniversal(String codigoUniversal);


}