package com.makernav.categorize.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.makernav.categorize.dto.ItemRequestDTO;
import com.makernav.categorize.dto.ItemResponseDTO;
import com.makernav.categorize.service.ItemFilter;
import com.makernav.categorize.service.ItemFilterType;
import com.makernav.categorize.service.ItemPdfService;
import com.makernav.categorize.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemPdfService itemPdfService;

    public ItemController(ItemService itemService, ItemPdfService itemPdfService) {
        this.itemService = itemService;
        this.itemPdfService = itemPdfService;
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponseDTO>> getAllItems(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "100") int pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status) {

        List<ItemFilter> filters = createFilters(name, type, category, status, null);
        Pageable pageable = PageRequest.of(Math.max(pageNumber, 0), Math.max(pageSize, 1));

        return ResponseEntity.ok(filters.isEmpty()
                ? itemService.getAllItems(pageable)
                : itemService.getItemsByFilters(filters, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable int id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createItem(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        var itemCriado = itemService.createItem(itemRequestDTO);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/items/{id}")
                .buildAndExpand(itemCriado.id())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable int id, @Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        itemService.updateItem(id, itemRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponseDTO>> searchByName(
            @RequestParam String nome,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "100") int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(pageNumber, 0), Math.max(pageSize, 1));
        return ResponseEntity.ok(itemService.getItemsByFilters(
                List.of(new ItemFilter(ItemFilterType.NAME, nome)), pageable));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportarPdf(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status) {

        List<ItemFilter> filters = createFilters(name, type, category, status, null);
        Page<ItemResponseDTO> itens = filters.isEmpty()
                ? itemService.getAllItems(Pageable.unpaged())
                : itemService.getItemsByFilters(filters, Pageable.unpaged());

        ByteArrayInputStream pdfStream = itemPdfService.gerarPdfItens(itens);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio-itens.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    private List<ItemFilter> createFilters(String name, String type, String category, String status, String quantity) {
        List<ItemFilter> filters = new ArrayList<>();
        addFilter(filters, ItemFilterType.NAME, name);
        addFilter(filters, ItemFilterType.TYPE, type);
        addFilter(filters, ItemFilterType.CATEGORY, category);
        addFilter(filters, ItemFilterType.STATUS, status);
        addFilter(filters, ItemFilterType.QUANTITY, quantity);
        return filters;
    }

    private void addFilter(List<ItemFilter> filters, ItemFilterType type, String value) {
        if (value != null && !value.isBlank()) {
            filters.add(new ItemFilter(type, value.trim()));
        }
    }
}
