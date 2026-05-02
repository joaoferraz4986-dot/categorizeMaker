package com.makernav.categorize.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(/projeto)
public class TelaDeProjetosController {

    //injeção de dependencia
    //ProjetoService não existe ainda
    private final ProjetoService projeto service;

    public TelaDeProjetosController(ProjetoService projetoService){
        this.projetoService = projetoService;
    }

}
