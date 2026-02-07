package com.badena.marketplace.services;

import java.util.List;
import com.badena.marketplace.entity.Producto;

public interface ProductService {

    List<Producto> findAll();

    Producto findById(Long id); 

    Producto save(Producto producto, Long idTienda);
    
    List<Producto> obtenerPorTienda(Long tiendaId);

    // Metodo para el buscador de market.html
    List<Producto> buscarGeneral(String consulta); 
}