package com.makernav.categorize.infra.repository;

import com.makernav.categorize.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByEmail(String email);

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
