package com.makernav.categorize.infra.repository;

import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllByEstado (Estado estado, Pageable pageable);
}
