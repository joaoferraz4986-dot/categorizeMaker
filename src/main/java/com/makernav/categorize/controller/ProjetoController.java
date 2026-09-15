package com.makernav.categorize.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.makernav.categorize.dto.ProjetoRequestDTO;
import com.makernav.categorize.dto.ProjetoResponseDTO;
import com.makernav.categorize.service.ProjetoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    public ProjetoResponseDTO criar(@Valid @RequestBody ProjetoRequestDTO projetoRequestDTO) {
        return projetoService.salvar(projetoRequestDTO);
    }

    @PutMapping("/{id}")
    public ProjetoResponseDTO atualizar(@PathVariable int id, @Valid @RequestBody ProjetoRequestDTO projetoRequestDTO) {
        return projetoService.atualizar(id, projetoRequestDTO);
    }

    @GetMapping("/ativos")
    public List<ProjetoResponseDTO> listarTodos() {
        return projetoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscar(@PathVariable int id) {
        return projetoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        projetoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
