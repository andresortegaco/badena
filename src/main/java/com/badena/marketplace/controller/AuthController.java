package com.badena.marketplace.controller;

import com.badena.marketplace.dto.LoginDTO;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.entity.TipoUsuario;
import com.badena.marketplace.entity.Usuario;
import com.badena.marketplace.repository.TiendaRepository;
import com.badena.marketplace.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }

        // Buscar si tiene una tienda asociada (si aplica)
        Optional<Tienda> tiendaOpt = tiendaRepository.findByUsuarioId(usuario.getId());

        // CONSTRUCCIÓN UNIFICADA DEL JSON PARA EL FRONTEND
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("tipo", usuario.getTipo() != null ? usuario.getTipo().name() : ""); // ADMIN o CORPORATION
        respuesta.put("tipoUsuario", usuario.getTipoUsuario() != null ? usuario.getTipoUsuario() : ""); // CORPORATION o PUBLISHER
        respuesta.put("fotoPerfil", usuario.getFotoPerfil());

        if (tiendaOpt.isPresent()) {
            respuesta.put("idTienda", tiendaOpt.get().getId());
            respuesta.put("nombreTienda", tiendaOpt.get().getNombrePublico());
        } else {
            respuesta.put("idTienda", null);
            respuesta.put("nombreTienda", "Sin Tienda");
        }

        return ResponseEntity.ok(respuesta);
    }

    // 1. Listar todos los usuarios con búsqueda robusta de tienda
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarTodosUsuarios() {
        try {
            List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
            
            List<Map<String, Object>> resultado = usuarios.stream().map(u -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", u.getId());
                map.put("nombre", u.getNombre());
                map.put("email", u.getEmail());
                map.put("tipo", u.getTipo() != null ? u.getTipo().name() : "");
                map.put("tipoUsuario", u.getTipoUsuario() != null ? u.getTipoUsuario() : "");
                
                String nombreTienda = "-";
                Long idTienda = null;

                // Opción A: Intentar obtener la tienda mediante el repositorio por ID de usuario
                Optional<Tienda> tiendaOpt = tiendaRepository.findByUsuarioId(u.getId());
                if (tiendaOpt.isPresent()) {
                    idTienda = tiendaOpt.get().getId();
                    nombreTienda = tiendaOpt.get().getNombrePublico();
                } 
                // Opción B: Si el objeto Usuario ya tiene la tienda asociada en memoria
                else if (u.getTienda() != null) {
                    idTienda = u.getTienda().getId();
                    nombreTienda = u.getTienda().getNombrePublico();
                }

                map.put("idTienda", idTienda);
                map.put("nombreTienda", nombreTienda);
                
                return map;
            }).toList();

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }


    // 2. Listar únicamente a los que tienen tipo_usuario = CORPORATION para la gestión de fabricantes
    @GetMapping("/corporations")
    public ResponseEntity<?> listarCorporations() {
        try {
            List<Usuario> allUsers = (List<Usuario>) usuarioRepository.findAll();
            List<Usuario> corporations = allUsers.stream()
                .filter(u -> "CORPORATION".equals(u.getTipoUsuario()))
                .toList();
            
            List<Map<String, Object>> resultado = corporations.stream().map(u -> {
                Optional<Tienda> tiendaOpt = tiendaRepository.findByUsuarioId(u.getId());
                Map<String, Object> map = new HashMap<>();
                map.put("id", u.getId());
                map.put("nombre", u.getNombre());
                map.put("email", u.getEmail());
                map.put("tipo", u.getTipo() != null ? u.getTipo().name() : "");
                map.put("tipoUsuario", u.getTipoUsuario());
                map.put("fotoPerfil", u.getFotoPerfil());
                if (tiendaOpt.isPresent()) {
                    map.put("idTienda", tiendaOpt.get().getId());
                    map.put("nombreTienda", tiendaOpt.get().getNombrePublico());
                } else {
                    map.put("idTienda", null);
                    map.put("nombreTienda", "Sin Tienda");
                }
                return map;
            }).toList();

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // 3. Crear un nuevo fabricante desde el Admin
    @PostMapping("/registro-fabricante")
    public ResponseEntity<?> registrarFabricante(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            if (usuarioRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(payload.get("nombre"));
            usuario.setEmail(email);
            usuario.setPassword(payload.get("password"));
            usuario.setTipo(TipoUsuario.CORPORATION);
            usuario.setTipoUsuario("CORPORATION"); // Asegurando el tipo_usuario interno
            usuario.setFechaRegistro(LocalDateTime.now());
            
            usuario = usuarioRepository.save(usuario);

            Tienda tienda = new Tienda();
            tienda.setNombrePublico(payload.get("nombreTienda"));
            tienda.setSlug(payload.get("slugTienda"));
            tienda.setUsuario(usuario);
            tienda = tiendaRepository.save(tienda);

            usuario.setTienda(tienda);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("mensaje", "Fabricante creado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al crear: " + e.getMessage()));
        }
    }
}