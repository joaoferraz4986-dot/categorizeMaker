package com.makernav.categorize.dto;

import com.makernav.categorize.model.Cargo;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID idUsuario,
        String nome,
        String email,
        Cargo cargo
) {}