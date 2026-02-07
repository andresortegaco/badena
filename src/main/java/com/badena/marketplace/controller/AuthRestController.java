package com.badena.marketplace.controller;

import com.badena.marketplace.dto.RegistroCompletoDTO;
import com.badena.marketplace.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") 
@CrossOrigin(origins = "*") 
public class AuthRestController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroCompletoDTO registroDTO) {
        try {
           usuarioService.registrar(registroDTO);
            
            return ResponseEntity.ok("{\"mensaje\": \"Registro exitoso\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}