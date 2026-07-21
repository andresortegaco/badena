package com.badena.marketplace.services;

import com.badena.marketplace.dto.RegistroCompletoDTO;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.entity.TipoUsuario;
import com.badena.marketplace.entity.Usuario;
import com.badena.marketplace.repository.TiendaRepository;
import com.badena.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importaciones nuevas para el manejo de archivos
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Transactional 
    public void registrar(RegistroCompletoDTO dto) {
        
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTipo(TipoUsuario.valueOf(dto.getTipo().toUpperCase()));
        usuario.setFechaRegistro(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        // Si es CORPORATION, crear su tienda inmediatamente
        if ("CORPORATION".equalsIgnoreCase(dto.getTipo())) {
            Tienda tienda = new Tienda();
            tienda.setNombrePublico(dto.getNombreTienda());
            tienda.setSlug(dto.getSlugTienda());
            tienda.setUsuario(usuario); // Vinculamos la tienda al dueño

            // Guardar la tienda en la tabla 'tiendas' save
            tienda = tiendaRepository.save(tienda);

            //Actualizar usuario con su tienda recién creada
            usuario.setTienda(tienda);
            usuarioRepository.save(usuario);
        }
    }

    /**
     * NUEVO MÉTODO: Actualiza la contraseña y/o la foto de perfil del usuario.
     */
    @Transactional
    public Usuario actualizarPerfil(Long id, String password, MultipartFile foto) throws IOException {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Si el usuario escribió una nueva contraseña, la actualizamos
        if (password != null && !password.trim().isEmpty()) {
            usuario.setPassword(password); 
            // Nota: Si usas encriptación en el futuro (ej. BCrypt), deberás encriptarla aquí antes de guardarla.
        }

        // Si el usuario subió una foto, la guardamos en el directorio 'uploads'
        if (foto != null && !foto.isEmpty()) {
            // Generar nombre único para evitar sobreescritura de archivos con el mismo nombre
            String nombreArchivo = System.currentTimeMillis() + "_" + foto.getOriginalFilename().replaceAll("\\s+", "_");
            
            // Crear la carpeta 'uploads' si no existe en la raíz del proyecto
            Path rutaDirectorio = Paths.get("uploads");
            if (!Files.exists(rutaDirectorio)) {
                Files.createDirectories(rutaDirectorio);
            }
            
            // Copiar el archivo físico
            Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
            Files.copy(foto.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            
            // Guardar solo el nombre del archivo en la base de datos
            usuario.setFotoPerfil(nombreArchivo);
        }

        return usuarioRepository.save(usuario);
    }
}