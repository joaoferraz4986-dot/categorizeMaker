package com.makernav.categorize.infra.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAll(Pageable pageable);

    Page<Item> findAll(Specification<Item> specification, Pageable pageable);

    // SELECT * FROM peca WHERE nome LIKE 'Abac%' | case unsensitive (ignoreCase)
    List<Item> findByNomeStartingWithIgnoreCase(String nome);

    @Query("SELECT i FROM Item i " +
            "WHERE (:categorias IS NULL OR i.categoria IN :categorias) " +
            "ORDER BY i.categoria ASC, i.nome ASC")
    List<Item> findByCategoriasEOrdenar(@Param("categorias") List<Categoria> categorias);
}
