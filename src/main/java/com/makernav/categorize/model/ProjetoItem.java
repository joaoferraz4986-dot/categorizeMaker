package com.makernav.categorize.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projeto_item")
@IdClass(ProjetoItemId.class)
public class ProjetoItem {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Projeto projeto;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private Item item;
    
    @Column(name = "quantidade_usada", nullable = false)
    private Integer quantidadeUsada = 0;
    
    @Column(name = "data_alocacao", nullable = false, updatable = false)
    private LocalDateTime dataAlocacao = LocalDateTime.now();

    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
    
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    
    public Integer getQuantidadeUsada() { return quantidadeUsada; }
    public void setQuantidadeUsada(Integer quantidadeUsada) { this.quantidadeUsada = quantidadeUsada; }
    
    public LocalDateTime getDataAlocacao() { return dataAlocacao; }
    public void setDataAlocacao(LocalDateTime dataAlocacao) { this.dataAlocacao = dataAlocacao; }
}