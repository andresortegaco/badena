package com.badena.marketplace.controller;

import com.badena.marketplace.dto.LoginDTO;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.entity.Usuario;
import com.badena.marketplace.repository.TiendaRepository;
import com.badena.marketplace.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TiendaRepository tiendaRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) {
        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        //Verificar contraseña 
        if (!usuario.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }

        //Buscar la Tienda de usuario
        Optional<Tienda> tiendaOpt = tiendaRepository.findByUsuarioId(usuario.getId());

        if (tiendaOpt.isEmpty()) {
            return ResponseEntity.status(403).body(Map.of("error", "Este usuario no tiene una tienda asignada"));
        }

        // Retornar los datos clave para el Frontend
        return ResponseEntity.ok(Map.of(
            "mensaje", "Login exitoso",
            "idUsuario", usuario.getId(),
            "nombreUsuario", usuario.getNombre(),
            "tipoUsuario", usuario.getTipo(),
            "idTienda", tiendaOpt.get().getId(), 
            "nombreTienda", tiendaOpt.get().getNombrePublico()
        ));
    }
}