package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ItemRequestDTO;
import com.makernav.categorize.dto.ItemResponseDTO;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.model.Categoria;
import com.makernav.categorize.model.Item;
import com.makernav.categorize.service.ItemPdfService;
import com.makernav.categorize.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemPdfService itemPdfService;
    private final ItemRepository itemRepository;

    public ItemController(ItemService itemService, ItemPdfService itemPdfService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemPdfService = itemPdfService;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById( @PathVariable int id) {
        return ResponseEntity.ok(itemService.getItemById(id) );
    }

    @PostMapping
    public ResponseEntity<Void> createItem( @Valid @RequestBody ItemRequestDTO itemRequestDTO ) {
        var itemCriado = itemService.createItem(itemRequestDTO);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path( "/api/items/{id}" )
                .buildAndExpand( itemCriado.id() )
                .toUri();

        return ResponseEntity.created( uri )
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateItem( @PathVariable int id, @Valid @RequestBody ItemRequestDTO itemRequestDTO ) {
        itemService.updateItem( id, itemRequestDTO );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem( @PathVariable int id ) {
        itemService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDTO>> searchByName( @RequestParam String nome ) {
        return ResponseEntity.ok( itemService.getItemsByName(nome) );
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportarPdf( @RequestParam(required = false) List<Categoria> categorias) {
        if (categorias != null && categorias.isEmpty()) {
            categorias = null;
        }

        List<Item> itens = itemRepository.findByCategoriasEOrdenar(categorias);
        ByteArrayInputStream pdfStream = itemPdfService.gerarPdfItens(itens);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=relatorio-itens.pdf");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdfStream));
    }
}