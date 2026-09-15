package com.makernav.categorize.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.makernav.categorize.infra.repository.EventoRepository;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Evento;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.TipoEvento;
import com.makernav.categorize.model.Usuario;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;
    private final ItemRepository itemRepository;

    public EventoService(EventoRepository eventoRepository, ItemRepository itemRepository) {
        this.eventoRepository = eventoRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void registrar(TipoEvento tipo, String entidade, Integer entidadeId, String titulo, String descricao, Usuario usuario) {
        Evento evento = new Evento();
        evento.setTipo(tipo);
        evento.setEntidade(entidade);
        evento.setEntidadeId(entidadeId);
        evento.setTitulo(titulo);
        evento.setDescricao(descricao);
        evento.setDataEvento(new Date());
        evento.setUsuario(usuario);
        var itens = itemRepository.findAll();
        evento.setTotalQuantidade(itens.stream().mapToInt(item -> item.getQuantidade()).sum());
        evento.setQuantidadeLivre(itens.stream().filter(item -> item.getEstado() == Estado.LIVRE).mapToInt(item -> item.getQuantidade()).sum());
        evento.setQuantidadeUsado(itens.stream().filter(item -> item.getEstado() == Estado.USADO).mapToInt(item -> item.getQuantidade()).sum());
        evento.setQuantidadeQuebrado(itens.stream().filter(item -> item.getEstado() == Estado.QUEBRADO).mapToInt(item -> item.getQuantidade()).sum());
        eventoRepository.save(evento);
    }
}
