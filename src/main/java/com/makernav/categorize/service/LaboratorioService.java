package com.makernav.categorize.service;

import com.makernav.categorize.dto.ItemDTO;
import com.makernav.categorize.dto.mapper.ItemMapper;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class LaboratorioService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public LaboratorioService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<Item> getTodosOsItens() {
        return itemRepository.findAll();
    }

    public Page<Item> getTodosPorEstado(Estado estado, Pageable pageable) {
        return itemRepository.findAllByEstado(estado, pageable);
    }

    public Item getItemPorId(int id) {
        return itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public URI criarItem(ItemDTO itemDTO) {
        var item = itemMapper.toEntity(itemDTO);

        itemRepository.save(item);

        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/items/{id}/")
                .buildAndExpand(item.getIdItem())
                .toUri();

        return uri;
    }

    @Transactional
    public void atualizarItem(int id, ItemDTO itemDTO) {
        var item = itemRepository.findById(id).orElseThrow();
        itemMapper.updateEntityFromDTO(itemDTO, item);
        itemRepository.save(item);
    }

    @Transactional
    public void deletarItem(int id) {
        itemRepository.deleteById(id);
    }
}
