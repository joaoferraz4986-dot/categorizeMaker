package com.makernav.categorize.service;

import com.makernav.categorize.dto.ItemDTOCriacao;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService {

    private final ItemRepository itemRepository;

    public LaboratorioService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getTodosOsItens() {
        return itemRepository.findAll();
    }

    public Page<Item> getTodosPorEstado(Estado estado, Pageable pageable) {
        return itemRepository.findAllByEstado(estado, pageable);
    }

    public Item getItemPorId(int id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void criarItem(ItemDTOCriacao itemDTOCriacao) {
        var item = new Item(itemDTOCriacao);
        itemRepository.save(item);
    }

    public void atualizarItem(int id, ItemDTOCriacao itemDTOCriacao) {

        var item = itemRepository.findById(id).orElseThrow();

        item.setCategoria(itemDTOCriacao.categoria());
        item.setNome(itemDTOCriacao.nome());
        item.setTipo(itemDTOCriacao.tipo());
        item.setQuantidade(itemDTOCriacao.quantidade());
        item.setEstado(itemDTOCriacao.estado());
        item.setImagem(itemDTOCriacao.imagem());

        itemRepository.save(item);
    }

    public void deletarItem(int id) {
        itemRepository.deleteById(id);
    }

    public List<Item> getItemPorNome(String nome){
        return itemRepository.findByNomeStartingWithIgnoreCase(nome);
    }
}
