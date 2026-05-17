package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ProjetoRequestDTO;
import com.makernav.categorize.dto.ProjetoResponseDTO;
import com.makernav.categorize.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService){
        this.projetoService = projetoService;
    }

    @PostMapping
    public ProjetoResponseDTO criar(@Valid @RequestBody ProjetoRequestDTO projetoRequestDTO){
        return projetoService.salvar(projetoRequestDTO);
    }

    @GetMapping
    public List<ProjetoResponseDTO> listarTodos() {
        return projetoService.listarTodos();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO>buscar(@PathVariable int id){
        return projetoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id){
        projetoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
