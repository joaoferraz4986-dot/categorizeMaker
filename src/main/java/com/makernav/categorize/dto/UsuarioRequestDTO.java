package com.makernav.categorize.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {}