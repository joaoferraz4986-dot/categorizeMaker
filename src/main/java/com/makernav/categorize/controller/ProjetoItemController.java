package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ProjetoItemRequestDTO;
import com.makernav.categorize.dto.ProjetoItemResponseDTO;
import com.makernav.categorize.service.ProjetoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos-itens")
@CrossOrigin(origins = "*")
public class ProjetoItemController {
    
    @Autowired
    private ProjetoItemService projetoItemService;
    
    @PostMapping
    public ResponseEntity<ProjetoItemResponseDTO> alocar(@Valid @RequestBody ProjetoItemRequestDTO dto) {
        try {
            ProjetoItemResponseDTO resultado = projetoItemService.alocar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/projetos/{idProjeto}")
    public ResponseEntity<List<ProjetoItemResponseDTO>> listarPorProjeto(@PathVariable Integer idProjeto) {
        List<ProjetoItemResponseDTO> resultado = projetoItemService.listarPorProjeto(idProjeto);
        return ResponseEntity.ok(resultado);
    }
}