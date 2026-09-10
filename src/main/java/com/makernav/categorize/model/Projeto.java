package com.makernav.categorize.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto")
    private int idProjeto;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CategoriaProjeto categoria;

    @Column(nullable = false)
    private String descricao;

    @Column()
    private Date dataInicio;

    @Column(name = "data_fim", nullable = true)
    private Date dataFim;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public String getNomeProjeto() {
        return this.nome;
    }
}
