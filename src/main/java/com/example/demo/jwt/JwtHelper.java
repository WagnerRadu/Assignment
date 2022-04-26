package com.example.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtHelper {
    Algorithm algorithm;

    public JwtHelper() {
        algorithm = Algorithm.HMAC256("secret"); //secret value could be given through application properties
    }

    public String generateToken(int userId) {
        return JWT.create()
                .withSubject(userId + "")
                .withIssuer("ath0")
                .sign(algorithm);
    }

    public String getUserId(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getSubject();
    }

    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("ath0")
                .build();
        return verifier.verify(token);
    }
}
