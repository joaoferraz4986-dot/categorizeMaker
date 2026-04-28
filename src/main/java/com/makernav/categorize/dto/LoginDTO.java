package com.makernav.categorize.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @Email @NotBlank String email,
        @NotBlank String senha
) {}
