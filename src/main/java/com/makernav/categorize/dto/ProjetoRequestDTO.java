package com.makernav.categorize.dto;

import java.util.List;

import com.makernav.categorize.model.CategoriaProjeto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjetoRequestDTO(
        @NotBlank String nome,
        @NotNull CategoriaProjeto categoria,
        @NotBlank String descricao,
        String imagem,
        @Valid List<ItemProjetoRequest> itens
) {
    public record ItemProjetoRequest(
            @NotNull Integer idItem,
            @Min(1) @Max(1000000) int quantidadeUsada
    ) {}
}
