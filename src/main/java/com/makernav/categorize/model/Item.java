package com.makernav.categorize.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "item" )
public class Item {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id_item" )
    private int idItem;

    @Column( nullable = false )
    @Enumerated( EnumType.STRING )
    private Categoria categoria;

    @Column( nullable = false, length = 255 )
    private String nome;

    @Column( length = 255 )
    private String tipo;

    @Column( nullable = false )
    private int quantidade;

    @Column( nullable = false )
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Lob
    @Column( columnDefinition = "LONGTEXT" )
    private String imagem;
}