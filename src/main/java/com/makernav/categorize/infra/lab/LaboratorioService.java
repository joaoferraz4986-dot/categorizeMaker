package com.makernav.categorize.infra.lab;

import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaboratorioService {

    @Autowired
    private final ItemRepository itemRepository;

    public Page<Item> getTodosPorEstado(Estado estado, Pageable pageable) {
        return itemRepository.findAllByEstado(estado, pageable);
    }
}
