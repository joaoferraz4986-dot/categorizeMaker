package com.makernav.categorize.dto;

import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDTO(@NotNull Categoria categoria, @NotBlank String nome, String tipo, @NotNull int quantidade, @NotNull Estado estado, String imagem) {}
