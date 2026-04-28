package com.makernav.categorize.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.makernav.categorize.model.Usuario;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTService {

    public String createToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC384("12345678");
        return JWT.create()
                .withIssuer("MakerNav")
                .withSubject(usuario.getNome())
                .withExpiresAt(expiracao(40))
                .sign(algorithm);

    }

    public String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC384("12345678");
            DecodedJWT decodedJWT =  JWT.require(algorithm)
                    .withIssuer("MakerNav")
                    .build().verify(token);

            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token invalido ou expirado!" + e.getMessage());
        }

    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }

}
