package com.badena.marketplace.services;

import com.badena.marketplace.entity.Categoria;
import com.badena.marketplace.dto.CategoriaRegistroDTO;
import com.badena.marketplace.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria crearCategoria(CategoriaRegistroDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        
        // Generar Slug (Ej: "Herramientas Eléctricas" -> "herramientas-electricas")
        String slug = dto.getNombre().toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", "")
                        .replaceAll("\\s+", "-");
        categoria.setSlug(slug);

        // Si enviaron un idPadre, lo asignamos (Es una subcategoría)
        if (dto.getIdPadre() != null) {
            Categoria padre = categoriaRepository.findById(dto.getIdPadre())
                .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setPadre(padre); // Ajustado al nombre real del setter
        }

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerArbolDeCategorias() {
        // Al traer las principales, Hibernate traerá sus subcategorías automáticamente
        return categoriaRepository.findByPadreIsNull(); // Ajustado al nombre real del repositorio
    }
}