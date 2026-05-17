package com.makernav.categorize.dto;

import com.makernav.categorize.model.CategoriaProjeto;

public record ProjetoResponseDTO(
        String nome,
        CategoriaProjeto categoriaProjeto,
        String descricao
) {}
