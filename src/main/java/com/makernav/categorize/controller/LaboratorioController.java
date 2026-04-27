package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ItemDTO;
import com.makernav.categorize.dto.mapper.ItemMapper;
import com.makernav.categorize.service.LaboratorioService;
import com.makernav.categorize.model.Item;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;
    private final ItemMapper itemMapper;

    public LaboratorioController(LaboratorioService laboratorioService, ItemMapper itemMapper) {
        this.laboratorioService = laboratorioService;
        this.itemMapper = itemMapper;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getTodosItens() {
        return ResponseEntity.ok(laboratorioService.getTodosOsItens()
                .stream()
                .map(itemMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemPorId(@PathVariable int id) {
        Item item = laboratorioService.getItemPorId(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemMapper.toDTO(item));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> criarItem(@Valid @RequestBody ItemDTO itemDTO) {
        var uri = laboratorioService.criarItem(itemDTO);

        return ResponseEntity.created(uri)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarItem(@PathVariable int id, @Valid @RequestBody ItemDTO itemDTO) {
        laboratorioService.atualizarItem(id, itemDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable int id) {
        laboratorioService.deletarItem(id);
        return ResponseEntity.notFound().build();
    }
}
