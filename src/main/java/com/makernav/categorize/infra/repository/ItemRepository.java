package com.makernav.categorize.infra.repository;

import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.makernav.categorize.model.Categoria;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllByEstado ( Estado estado, Pageable pageable );

    // SELECT * FROM peca WHERE nome LIKE 'Abac%'  | case unsensitive (ignoreCase)
    List<Item> findByNomeStartingWithIgnoreCase( String nome );

    @Query("SELECT i FROM Item i " +
           "WHERE (:categorias IS NULL OR i.categoria IN :categorias) " +
           "ORDER BY i.categoria ASC, i.nome ASC")
    List<Item> findByCategoriasEOrdenar(@Param("categorias") List<Categoria> categorias);
}
