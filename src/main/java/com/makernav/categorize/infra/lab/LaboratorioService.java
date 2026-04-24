package com.makernav.categorize.infra.lab;

import com.makernav.categorize.dto.ItemDTOCriacao;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LaboratorioService {

    @Autowired
    private final ItemRepository itemRepository;

    public LaboratorioService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> getTodosPorEstado(Estado estado, Pageable pageable) {
        return itemRepository.findAllByEstado(estado, pageable);
    }

    public void criarItem(ItemDTOCriacao itemDTOCriacao) {
        var item = new Item(itemDTOCriacao);

        itemRepository.save(item);
    }

    public void deletarItem(int id) {
        itemRepository.deleteById(id);
    }
}
