package com.apnihaveli.artiStick.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class jwt {

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateJwt(String payload) {
        return Jwts.builder().setSubject(payload).signWith(key).compact();
    }

    public static boolean verifyJwt(String payload, String equality ) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(payload);
            return claims.getBody().getSubject().equals(equality);
        } catch (JwtException e) {
            return false;
        }
    }
}
