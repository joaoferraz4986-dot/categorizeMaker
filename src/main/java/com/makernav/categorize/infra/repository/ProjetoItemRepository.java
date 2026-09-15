package com.makernav.categorize.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makernav.categorize.model.ProjetoItem;

public interface ProjetoItemRepository extends JpaRepository<ProjetoItem, Integer> {
    List<ProjetoItem> findByProjetoIdProjeto(Integer projetoId);
    void deleteByProjetoIdProjeto(Integer projetoId);
}
