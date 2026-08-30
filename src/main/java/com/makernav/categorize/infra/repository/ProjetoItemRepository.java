package com.makernav.categorize.infra.repository;

import com.makernav.categorize.model.ProjetoItem;
import com.makernav.categorize.model.ProjetoItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoItemRepository extends JpaRepository<ProjetoItem, ProjetoItemId> {
    
    // Buscar todos os itens de um projeto
    @Query("SELECT pi FROM ProjetoItem pi WHERE pi.projeto.id = :idProjeto")
    List<ProjetoItem> findByProjetoId(@Param("idProjeto") Integer idProjeto);    
}