package com.makernav.categorize.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.makernav.categorize.dto.ItemRequestDTO;
import com.makernav.categorize.dto.ItemResponseDTO;
import com.makernav.categorize.dto.mapper.ItemMapper;
import com.makernav.categorize.infra.exception.FilterNotFoundExceptions;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public Page<ItemResponseDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(itemMapper::toResponseDTO);
    }

    public Page<ItemResponseDTO> getItemsByFilters(List<ItemFilter> filters, Pageable pageable) {
        if (filters == null || filters.isEmpty()) {
            return getAllItems(pageable);
        }

        List<Specification<Item>> specifications = new ArrayList<>();

        for (ItemFilter filter : filters) {
            if (filter == null || filter.filter() == null || filter.filter().isBlank()) {
                continue;
            }

            String value = filter.filter().trim();
            switch (filter.type()) {
                case NAME -> specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + value.toLowerCase() + "%"));

                case TYPE -> specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("tipo")),
                        value.toLowerCase()));

                case CATEGORY -> specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                        .equal(root.get("categoria"), Categoria.valueOf(value.toUpperCase())));

                case QUANTITY -> specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                        .equal(root.get("quantidade"), Integer.parseInt(value)));

                case STATUS -> specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                        .equal(root.get("estado"), Estado.valueOf(value.toUpperCase())));
            }
        }

        Specification<Item> combined = specifications.stream()
                .reduce(Specification::and)
                .orElseThrow(() -> new FilterNotFoundExceptions("Nenhum filtro válido foi informado"));

        return itemRepository.findAll(combined, pageable).map(itemMapper::toResponseDTO);
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

    public List<ItemResponseDTO> getItemsByName(String nome) {
        return itemRepository.findByNomeStartingWithIgnoreCase(nome).stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }
}
