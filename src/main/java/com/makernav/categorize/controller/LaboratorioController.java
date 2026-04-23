package com.makernav.categorize.controller;

import com.makernav.categorize.infra.lab.LaboratorioService;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Item;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/MakerNav/lab/")
@RequiredArgsConstructor
public class LaboratorioController {

    @Autowired
    private final LaboratorioService laboratorioService;

    @GetMapping
    public String getPage() {
        return "../../resources/static/lab.html";
    }

    @GetMapping("/todos/")
    public Page<Item> getTodosItensPorEstado(@Valid @RequestParam Estado estado, Pageable pageable) {
        return laboratorioService.getTodosPorEstado(estado, pageable);
    }
}
