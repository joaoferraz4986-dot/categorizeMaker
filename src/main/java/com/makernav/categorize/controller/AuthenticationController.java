package com.makernav.categorize.controller;

import com.makernav.categorize.dto.LoginDTO;
import com.makernav.categorize.dto.TokenDTO;
import com.makernav.categorize.dto.UsuarioRequestDTO;
import com.makernav.categorize.dto.UsuarioResponseDTO;
import com.makernav.categorize.infra.security.AuthenticationRateLimiter;
import com.makernav.categorize.model.Usuario;
import com.makernav.categorize.service.AuthenticationService;
import com.makernav.categorize.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final AuthenticationRateLimiter rateLimiter;

    public AuthenticationController(AuthenticationService authenticationService,
                                    AuthenticationManager authenticationManager,
                                    JWTService jwtService,
                                    AuthenticationRateLimiter rateLimiter) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/registro/")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO,
                                                       HttpServletRequest request ) {
        rateLimiter.validate( getClientAddress(request) );

        var usuarioCriado = authenticationService.createUsuario( usuarioRequestDTO );

        return ResponseEntity.ok( usuarioCriado );
    }

    @PostMapping("/login/")
    public ResponseEntity<TokenDTO> login( @Valid @RequestBody LoginDTO loginDTO,
                                          HttpServletRequest request ) {
        rateLimiter.validate( getClientAddress(request) );

        var authenticationToken = new UsernamePasswordAuthenticationToken( loginDTO.email(), loginDTO.senha() );
        var authentication = authenticationManager.authenticate( authenticationToken );
        var token = jwtService.createToken( (Usuario) authentication.getPrincipal() );

        return ResponseEntity.ok( new TokenDTO(token) );
    }

    private String getClientAddress( HttpServletRequest request ) {

        var forwarded = request.getHeader( "X-Forwarded-For" );

        if ( forwarded != null && !forwarded.isBlank() ) {
            return forwarded.split( "," )[0].trim();
        }

        return request.getRemoteAddr();
    }

}
