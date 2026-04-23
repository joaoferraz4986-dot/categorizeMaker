package com.makernav.categorize.dto;

import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDTOCriacao(@NotNull Categoria categoria, @NotBlank String nome, String tipo, @NotNull int quantidade, @NotNull Estado estado, String imagem) {
    public Categoria getCategoria() {
        return this.categoria;
    }
    public String getNome() {
        return this.nome;
    }
    public String getTipo() {
        return this.tipo;
    }
    public int getQuantidade() {
        return this.quantidade;
    }
    public Estado getEstado() {
        return this.estado;
    }
    public String getImagem() {
        return this.imagem;
    }

}
