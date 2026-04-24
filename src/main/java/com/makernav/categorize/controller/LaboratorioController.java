package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ItemDTOCriacao;
import com.makernav.categorize.infra.lab.LaboratorioService;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/MakerNav/lab/")
public class LaboratorioController {

    @Autowired
    private final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    public String getPage() {
        return "../../resources/static/lab.html";
    }

    @GetMapping("/todos/")
    public Page<Item> getTodosItensPorEstado( @RequestParam Estado estado, Pageable pageable ) {
        return laboratorioService.getTodosPorEstado(estado, pageable);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> criarItem(@Valid @RequestBody ItemDTOCriacao itemDTOCriacao) {
        laboratorioService.criarItem(itemDTOCriacao);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deletarItemPorId(int id) {
        laboratorioService.deletarItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
