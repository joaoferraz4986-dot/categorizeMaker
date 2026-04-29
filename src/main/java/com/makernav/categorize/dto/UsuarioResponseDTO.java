package com.makernav.categorize.dto;

import com.makernav.categorize.model.Cargo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID idUsuario,
        @NotBlank String nome,
        @NotBlank @Email String email,
        Cargo cargo
) {}