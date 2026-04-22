package com.makernav.categorize.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class LandingPageController {
    @GetMapping
    public String getPage() {
        return "../../resources/index.html";
    }
}
