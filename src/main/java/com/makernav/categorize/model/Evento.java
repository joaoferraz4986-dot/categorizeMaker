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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoEvento tipo;

    @Column(nullable = false, length = 50)
    private String entidade;

    @Column(name = "entidade_id")
    private Integer entidadeId;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 255)
    private String descricao;

    @Column(name = "data_evento")
    private Date dataEvento;

    @Column(name = "total_quantidade")
    private Integer totalQuantidade;

    @Column(name = "quantidade_livre")
    private Integer quantidadeLivre;

    @Column(name = "quantidade_usado")
    private Integer quantidadeUsado;

    @Column(name = "quantidade_quebrado")
    private Integer quantidadeQuebrado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
