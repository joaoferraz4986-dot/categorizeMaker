package com.makernav.categorize.service;

import com.makernav.categorize.dto.ItemRequestDTO;
import com.makernav.categorize.dto.ItemResponseDTO;
import com.makernav.categorize.dto.mapper.ItemMapper;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<ItemResponseDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    public Page<ItemResponseDTO> getItemsByState(Estado estado, Pageable pageable) {
        return itemRepository.findAllByEstado(estado, pageable)
                .map(itemMapper::toResponseDTO);
    }

    public ItemResponseDTO getItemById(int id) {
        var item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return itemMapper.toResponseDTO(item);
    }

    @Transactional
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO) {
        var item = itemMapper.toEntity(itemRequestDTO);

        itemRepository.save(item);

        return itemMapper.toResponseDTO(item);
    }

    @Transactional
    public void updateItem(int id, ItemRequestDTO itemRequestDTO) {
        var item = itemRepository.findById(id).orElseThrow();
        itemMapper.updateEntityFromDTO(itemRequestDTO, item);
        itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    public List<ItemResponseDTO> getItemsByName(String nome){
        return itemRepository.findByNomeStartingWithIgnoreCase(nome)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }
}
