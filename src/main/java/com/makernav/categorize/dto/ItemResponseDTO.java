package com.makernav.categorize.dto;

import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Estado;

public record ItemResponseDTO(
        Integer id,
        Categoria categoria,
        String nome,
        String tipo,
        int quantidade,
        Estado estado,
        String imagem
) {}