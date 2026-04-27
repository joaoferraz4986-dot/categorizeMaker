package com.makernav.categorize.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UsuarioDTO(
        UUID idUsuario,
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {}
