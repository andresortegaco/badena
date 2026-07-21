package com.badena.marketplace.controller; 

import com.badena.marketplace.entity.Marca;
import com.badena.marketplace.repository.MarcaRepository;
import com.badena.marketplace.services.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private MarcaService marcaService;

    // GET: Listar todas las marcas del sistema
    @GetMapping
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    // GET: Listar las marcas asociadas a una tienda específica[cite: 9, 10]
    @GetMapping("/tienda/{tiendaId}")
    public List<Marca> listarPorTienda(@PathVariable Long tiendaId) {
        return marcaRepository.findByTiendaId(tiendaId);
    }

    // POST: Crear una nueva marca recibiendo FormData (Nombre, Descripción, Tienda y Foto/Logo)
    @PostMapping
    public ResponseEntity<?> crearMarca(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("idTienda") Long idTienda,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {
        try {
            // Delega la lógica de negocio, slug, asociación y manejo de archivos al servicio
            Marca nuevaMarca = marcaService.crearMarcaConFoto(nombre, descripcion, idTienda, foto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}