package com.badena.marketplace.repository;

import com.badena.marketplace.entity.TipoUsuario;
import com.badena.marketplace.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    

    // Buscar un usuario donde el campo 'email' coincida
    Optional<Usuario> findByEmail(String email);
    
    // Contar para la vista de ADMIN (Todos los publicadores del sistema)
    long countByTipo(TipoUsuario tipo);
    

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tienda.id = :idTienda AND u.tipo = :tipo")
    long contarPublicadoresDeMiTienda(@Param("idTienda") Long idTienda, @Param("tipo") TipoUsuario tipo);


    
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tienda.id = :idTienda AND u.tipo = :tipo")
    long contarPorTiendaAndTipo(@Param("idTienda") Long idTienda, @Param("tipo") TipoUsuario tipo);
    List<Usuario> findByTiendaIdAndTipoUsuario(Long tiendaId, String tipoUsuario);



    // 
    List<Usuario> findByTipo(com.badena.marketplace.entity.TipoUsuario tipo);
}

   
