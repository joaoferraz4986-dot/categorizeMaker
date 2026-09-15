package com.makernav.categorize.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makernav.categorize.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findAllByOrderByDataEventoDesc();
    List<Evento> findTop8ByOrderByDataEventoDesc();
}
