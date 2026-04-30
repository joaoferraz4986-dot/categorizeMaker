package com.makernav.categorize.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.makernav.categorize.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken( Usuario usuario ) {
        Algorithm algorithm = Algorithm.HMAC384( secret );

        return JWT.create()
                .withIssuer("MakerNav")
                .withSubject(usuario.getEmail())
                .withExpiresAt(expiracao(90))
                .sign(algorithm);
    }

    public String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC384( secret );
            DecodedJWT decodedJWT = JWT.require( algorithm )
                    .withIssuer( "MakerNav" )
                    .build().verify( token );

            return decodedJWT.getSubject();
        } catch ( JWTVerificationException e ) {
            throw new JWTVerificationException( "Token invalido ou expirado! " + e.getMessage() );
        }
    }

    private Instant expiracao( Integer minutos ) {
        return Instant.now().plus( Duration.ofMinutes(minutos) );
    }

}
