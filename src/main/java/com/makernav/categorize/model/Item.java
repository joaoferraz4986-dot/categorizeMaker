package com.makernav.categorize.model;

import com.makernav.categorize.dto.ItemDTOCriacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    public Item(ItemDTOCriacao itemDTO) {
        this.categoria = itemDTO.getCategoria();
        this.nome = itemDTO.getNome();
        this.tipo = itemDTO.getTipo();
        this.quantidade = itemDTO.getQuantidade();
        this.estado = itemDTO.getEstado();
        this.imagemItem = itemDTO.getImagem();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotBlank
    private String nome;

    private String tipo;

    @NotNull
    private int quantidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Lob
    private String imagemItem;
}
