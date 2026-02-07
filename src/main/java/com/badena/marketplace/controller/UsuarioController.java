package com.badena.marketplace.controller;

import com.badena.marketplace.entity.Usuario;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.repository.UsuarioRepository;
import com.badena.marketplace.repository.TiendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    //Obtener los datos de un usuario
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long id) {
        if (id == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID de usuario es obligatorio"));
            }

        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return usuarioRepository.findByEmail(email)
            .map(user -> {
                if (user.getPassword().equals(password)) {
                    // CONSTRUCCIÓN MANUAL: Esto evita que Jackson intente navegar por las relaciones
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("id", user.getId());
                    respuesta.put("nombre", user.getNombre());
                    respuesta.put("email", user.getEmail());
                    respuesta.put("tipo", user.getTipo()); // Nivel 1
                    respuesta.put("tipoUsuario", user.getTipoUsuario()); // Nivel 2 (PUBLISHER)
                    
                    if (user.getTienda() != null) {
                        respuesta.put("idTienda", user.getTienda().getId());
                        respuesta.put("nombreTienda", user.getTienda().getNombrePublico());
                    }

                    return ResponseEntity.ok(respuesta);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("error", "Credenciales incorrectas"));
                }
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "El correo no está registrado")));
    }





    // Listar los publicadores de una tienda (Solo para CORPORATION)
    @GetMapping("/tienda/{tiendaId}/publishers")
    public ResponseEntity<List<Usuario>> listarPublishers(@PathVariable Long tiendaId) {
        return ResponseEntity.ok(usuarioRepository.findByTiendaIdAndTipoUsuario(tiendaId, "PUBLISHER"));
    }

    // Crear un Publisher (Asociado a la tienda del Corporation)
    @PostMapping("/crear-publisher")
        public ResponseEntity<?> crearPublisher(@RequestBody Map<String, String> payload) {
            try {
                Usuario nuevoPub = new Usuario();
                
                // Asignar los datos del JSON a la entidad
                nuevoPub.setNombre(payload.get("nombre"));
                nuevoPub.setEmail(payload.get("email"));
                nuevoPub.setPassword(payload.get("password"));
                
                // Campos de control de la base de datos
                nuevoPub.setTipoUsuario("PUBLISHER");
                nuevoPub.setTipoUsuario("PUBLISHER");
                nuevoPub.setFechaRegistro(LocalDateTime.now());

                // Vincular la tienda de la empresa
                Long idTienda = Long.parseLong(payload.get("idTienda"));
                Tienda tienda = tiendaRepository.findById(idTienda)
                        .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
                nuevoPub.setTienda(tienda);

                usuarioRepository.save(nuevoPub);

                return ResponseEntity.ok(Map.of("mensaje", "Publicador creado con éxito"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }


    @PutMapping("/actualizar-perfil/{id}")
        public ResponseEntity<?> actualizarPerfil(@PathVariable Long id, @RequestBody Map<String, String> datos) {

            if (id == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID de usuario es obligatorio"));
            }

            try {

                Usuario usuario = usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // para actualizar la contraseña
                String nuevaClave = datos.get("password");
                
                if (nuevaClave != null && !nuevaClave.trim().isEmpty()) {
                    usuario.setPassword(nuevaClave); 
                    usuarioRepository.save(usuario);
                    
                    return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "La contraseña no puede estar vacía"));
                }

            } catch (Exception e) {

                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        } 


        
        @PutMapping("/update-pass")
        public ResponseEntity<?> actualizarPassword(@RequestBody Map<String, Object> payload) {
            try {
                // se traen y validan datos del JSON
                if (!payload.containsKey("id") || !payload.containsKey("password")) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos obligatorios"));
                }

                Long id = Long.valueOf(payload.get("id").toString());
                String nuevaPass = payload.get("password").toString();

                if (nuevaPass.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La contraseña no puede estar vacía"));
                }

                // Buscar el usuario en la base de datos
                Usuario usuario = usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

                //Actualizar la contraseña
                usuario.setPassword(nuevaPass); 
                
                //Guardar cambios.
                usuarioRepository.save(usuario);

                System.out.println("Contraseña actualizada para el usuario: " + usuario.getEmail());
                
                return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));

            } catch (Exception e) {
                System.err.println("Error al actualizar pass: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error interno al procesar la solicitud"));
            }
        }
        






}