package com.makernav.categorize.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaPrincipalController {

    @GetMapping("/")
    public String getIndexPage() {
        return "forward:/index.html";
    }

    @GetMapping("/authentication/registro")
    public String getRegistroPage() {
        return "forward:/Registro.html";
    }

    @GetMapping("/lab")
    public String getLabPage() {
        return "forward:/lab.html";
    }
}
