package com.makernav.categorize.model;

import com.makernav.categorize.dto.ItemDTOCriacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Entity
public class Item {

    public Item() {
    }

    public Item(int id, Categoria categoria, String nome, String tipo, int quantidade, Estado estado, String imagem) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.estado = estado;
        this.imagem = imagem;
    }

    public Item(ItemDTOCriacao itemDTO) {
        this.categoria = itemDTO.categoria();
        this.nome = itemDTO.nome();
        this.tipo = itemDTO.tipo();
        this.quantidade = itemDTO.quantidade();
        this.estado = itemDTO.estado();
        this.imagem = itemDTO.imagem();
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
    private String imagem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
