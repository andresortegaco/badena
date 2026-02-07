package com.badena.marketplace.controller; 

import com.badena.marketplace.entity.Categoria;
import com.badena.marketplace.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "*") 
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}