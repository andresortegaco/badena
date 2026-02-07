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
}