package com.makernav.categorize.controller;

import com.makernav.categorize.dto.UsuarioDTO;
import com.makernav.categorize.service.AuthenticationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Transactional
    @PostMapping("/registrar/")
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var usuarioCriado = authenticationService.createUsuario(usuarioDTO);
        return ResponseEntity.ok(usuarioCriado);
    }

}
