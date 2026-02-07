package com.badena.marketplace.controller;

import com.badena.marketplace.entity.TipoUsuario;
import com.badena.marketplace.repository.ProductoRepository;
import com.badena.marketplace.repository.UsuarioRepository; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/stats/{idTienda}")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable Long idTienda) {
        try {
            // Caso 1: Cuando es ADMIN o MARKET (idTienda es 0 o null).
            if (idTienda == null || idTienda == 0) {
                return ResponseEntity.ok(Map.of(
                    "totalProductos", productoRepository.count(),
                    "totalPublicadores", usuarioRepository.countByTipo(TipoUsuario.PUBLISHER)
                ));
            }
            
            // Caso 2: Cuando es CORPORATION o PUBLISHER de una tienda.
            long prods = productoRepository.contarProductosDeMiTienda(idTienda);
            long pubs = usuarioRepository.contarPorTiendaAndTipo(idTienda, TipoUsuario.PUBLISHER);

            return ResponseEntity.ok(Map.of(
                "totalProductos", prods,
                "totalPublicadores", pubs
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}