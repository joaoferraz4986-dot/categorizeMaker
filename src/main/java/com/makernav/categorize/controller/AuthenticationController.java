package com.makernav.categorize.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.makernav.categorize.dto.LoginDTO;
import com.makernav.categorize.dto.TokenDTO;
import com.makernav.categorize.dto.UsuarioDTO;
import com.makernav.categorize.model.Usuario;
import com.makernav.categorize.service.AuthenticationService;
import com.makernav.categorize.service.JWTService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authentication/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/registro")
    public String getRegistroPage() {
        return "forward:/Registro.html";
    }

    @PostMapping("/registrar/")
    @ResponseBody
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var usuarioCriado = authenticationService.createUsuario(usuarioDTO);
        return ResponseEntity.ok(usuarioCriado);
    }

    @PostMapping("/login/")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var token = jwtService.createToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(token));
    }

}
