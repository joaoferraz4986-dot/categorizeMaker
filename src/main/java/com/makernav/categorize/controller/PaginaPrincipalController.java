package com.makernav.categorize.controller;

import com.makernav.categorize.dto.ItemDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/MakerNav")
public class PaginaPrincipalController {

    @GetMapping
    public String getPage() {
        return "../../resources/principal.html";
    }
}
