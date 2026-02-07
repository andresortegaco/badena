package com.badena.marketplace.controller;

import com.badena.marketplace.entity.Marca;
import com.badena.marketplace.repository.MarcaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tiendas")
@CrossOrigin(origins = "*")
public class TiendaController {

    @Autowired
    private MarcaRepository marcaRepository;

    // Endpoint para llenar el select de Marcas en add_product.html
    // el GET para http://localhost:8080/api/tiendas/1/marcas
    @GetMapping("/{idTienda}/marcas")
    public List<Marca> listarMarcasPorTienda(@PathVariable Long idTienda) {
        return marcaRepository.findByTiendaId(idTienda);
    }
}