package com.makernav.categorize.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaPrincipalController {

    @GetMapping("/")
    public String getIndexPage() {
        return "forward:/index.html";
    }
}
