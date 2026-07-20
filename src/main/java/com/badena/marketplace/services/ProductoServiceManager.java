package com.badena.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.stereotype.Service;

import com.badena.marketplace.entity.Producto;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.repository.ProductoRepository; 
import com.badena.marketplace.repository.TiendaRepository;
import com.badena.marketplace.dto.*;


@Service
public class ProductoServiceManager implements ProductService {

    
    @Autowired
    private ProductoRepository productoRepository; 

    @Autowired
    private TiendaRepository tiendaRepository; 

    @Override
        @Transactional 
        public Producto save(Producto producto, Long idTienda) { 
            Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> new RuntimeException("No se puede guardar el producto: La tienda con ID " + idTienda + " no existe."));
                
            
            producto.setTienda(tienda);
            
            return productoRepository.save(producto);
        }
    @Override
        public List<Producto> obtenerPorTienda(Long tiendaId) {
            return productoRepository.findByTiendaId(tiendaId);
        }
            


    @Override
        public List<Producto> findAll() {
            return productoRepository.findAll();
        }

    @Override
        public Producto findById(Long id) {
            return productoRepository.findById(id).orElse(null);
        }

    @Override
        public List<Producto> buscarGeneral(String consulta) {
            return productoRepository.findByNombreProductoContainingIgnoreCaseOrCodigoUniversalContainingIgnoreCase(
                consulta, consulta);
        }




    public ProductoDetalleDTO obtenerDetallePorEan(String ean) {
    Producto p = productoRepository.findByCodigoUniversal(ean)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    ProductoDetalleDTO dto = new ProductoDetalleDTO();
    dto.setNombre(p.getNombreProducto());
    dto.setCodigoUniversal(p.getCodigoUniversal());
    dto.setNombreMarca(p.getMarca().getNombre());
    
    dto.setNombreTienda(p.getTienda().getNombrePublico());
    
    dto.setCategoria(p.getCategoria().getNombre());
    dto.setDescripcionMarketing(p.getDescripcionMarketing());
    dto.setDescripcionTecnica(p.getDescripcionTecnica());
    dto.setPesoKg(p.getPesoKg());
    
    return dto;
}
}