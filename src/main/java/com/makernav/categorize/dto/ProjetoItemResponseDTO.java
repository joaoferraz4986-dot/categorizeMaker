package com.makernav.categorize.dto;

import java.time.LocalDateTime;

public record ProjetoItemResponseDTO(
    Integer idProjeto,
    String nomeProjeto,
    Integer idItem,
    String nomeItem,
    Integer quantidadeUsada,
    LocalDateTime dataAlocacao
) {}