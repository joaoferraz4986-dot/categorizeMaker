package com.makernav.categorize.dto;

import com.makernav.categorize.model.CategoriaProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjetoRequestDTO(
    @NotBlank String nome,
    @NotNull CategoriaProjeto categoria,
    @NotBlank String descricao
) {}
