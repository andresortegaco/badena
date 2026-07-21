package com.badena.marketplace.services;

import com.badena.marketplace.entity.Marca;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.repository.MarcaRepository;
import com.badena.marketplace.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public Marca crearMarcaConFoto(String nombre, String descripcion, Long idTienda, MultipartFile foto) throws IOException {
        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre(nombre);
        nuevaMarca.setDescripcion(descripcion);
        
        // Generar Slug automáticamente a partir del nombre
        String slug = nombre.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
        nuevaMarca.setSlug(slug);
        
        Tienda tienda = tiendaRepository.findById(idTienda)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        nuevaMarca.setTienda(tienda);

        // Procesar y guardar la foto/logo si viene adjunta
        if (foto != null && !foto.isEmpty()) {
            byte[] bytes = foto.getBytes();
            String nombreArchivo = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
            Path rutaCompleta = Paths.get(UPLOAD_DIR + nombreArchivo);
            
            Files.createDirectories(rutaCompleta.getParent());
            Files.write(rutaCompleta, bytes);

            // Asigna el nombre del archivo al campo logo de la entidad Marca
            nuevaMarca.setLogo(nombreArchivo); 
        }

        return marcaRepository.save(nuevaMarca);
    }
}