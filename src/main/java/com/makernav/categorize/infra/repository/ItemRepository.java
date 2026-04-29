package com.makernav.categorize.infra.repository;

import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllByEstado ( Estado estado, Pageable pageable );

    // SELECT * FROM peca WHERE nome LIKE 'Abac%'  | case unsensitive (ignoreCase)
    List<Item> findByNomeStartingWithIgnoreCase( String nome );
}
