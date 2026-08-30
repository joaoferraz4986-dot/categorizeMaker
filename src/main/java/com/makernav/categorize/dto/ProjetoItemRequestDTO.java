package com.makernav.categorize.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProjetoItemRequestDTO(
    @NotNull Integer idProjeto,
    @NotNull Integer idItem,
    @NotNull @Positive Integer quantidadeUsada
) {}