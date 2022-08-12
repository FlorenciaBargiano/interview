package com.interview.bci.configuration;

import com.interview.bci.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {

    private final String jwtSecret = "secret";

    public String generateJwtToken(User user) {
        final long TOKEN_VALIDITY = 2 * 60 * 60;

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setId(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String geIdFromToken(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getId();
    }

}
