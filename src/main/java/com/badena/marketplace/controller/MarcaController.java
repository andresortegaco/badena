package com.badena.marketplace.controller; 

import com.badena.marketplace.entity.Marca;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.repository.MarcaRepository;
import com.badena.marketplace.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    // metodo para listar las marcas por tienda.
    @GetMapping("/tienda/{tiendaId}")
    public List<Marca> listarPorTienda(@PathVariable Long tiendaId) {
        return marcaRepository.findByTiendaId(tiendaId);
    }

    @Autowired
    private TiendaRepository tiendaRepository;
    @PostMapping
    public ResponseEntity<?> crearMarca(@RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        Long idTienda = Long.valueOf(payload.get("idTienda").toString());

        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre(nombre);
        
        // asignar la marca a la tienda.
        Tienda tienda = tiendaRepository.findById(idTienda).orElseThrow();
        nuevaMarca.setTienda(tienda);

        marcaRepository.save(nuevaMarca);
        return ResponseEntity.ok(Map.of("mensaje", "Marca creada exitosamente"));
    }

}