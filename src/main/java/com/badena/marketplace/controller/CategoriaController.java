package com.badena.marketplace.controller; 

import com.badena.marketplace.entity.Categoria;
import com.badena.marketplace.dto.CategoriaRegistroDTO;
import com.badena.marketplace.services.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "*") 
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // POST: Crear una categoría principal o subcategoría
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody CategoriaRegistroDTO dto) {
        Categoria nueva = categoriaService.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // GET: Listar todo el árbol (solo trae las principales, las subcategorías vienen anidadas)
    @GetMapping
    public ResponseEntity<List<Categoria>> listarArbol() {
        List<Categoria> arbol = categoriaService.obtenerArbolDeCategorias();
        return ResponseEntity.ok(arbol);
    }
}