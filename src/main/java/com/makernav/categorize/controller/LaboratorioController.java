package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ItemDTOCriacao;
import com.makernav.categorize.infra.lab.LaboratorioService;
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

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getTodosItens() {
        return ResponseEntity.ok(laboratorioService.getTodosOsItens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemPorId(@PathVariable int id) {
        Item item = laboratorioService.getItemPorId(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> criarItem(@Valid @RequestBody ItemDTOCriacao itemDTOCriacao) {
        laboratorioService.criarItem(itemDTOCriacao);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> atualizarItem(@PathVariable int id, @Valid @RequestBody ItemDTOCriacao itemDTOCriacao) {
        laboratorioService.atualizarItem(id, itemDTOCriacao);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarItem(@PathVariable int id) {
        laboratorioService.deletarItem(id);
        return ResponseEntity.ok().build();
    }
}
