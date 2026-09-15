package com.makernav.categorize.dto;

import java.util.List;

import com.makernav.categorize.model.CategoriaProjeto;

public record ProjetoResponseDTO(
        Integer id,
        String nome,
        CategoriaProjeto categoriaProjeto,
        String descricao,
        String imagem,
        List<ItemProjetoResponse> itens
) {
    public record ItemProjetoResponse(
            Integer id,
            String nome,
            int quantidadeUsada
    ) {}
}
