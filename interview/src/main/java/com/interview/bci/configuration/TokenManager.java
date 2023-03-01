package com.interview.bci.configuration;

import com.interview.bci.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {

    @Value("${token.manager.jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(User user) {
        final long TOKEN_VALIDITY = 2 * 60;

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setId(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, decryptJWTSecret()).compact();
    }

    public String geIdFromToken(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(decryptJWTSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getId();
    }

    public boolean isTokenExpired(String token) {
        final Date dateExpiration = Jwts.parser()
                .setSigningKey(decryptJWTSecret())
                .parseClaimsJws(token)
                .getBody().getExpiration();

        return dateExpiration.before(new Date());
    }

    private String decryptJWTSecret() {
        return UriUtils.decode(jwtSecret, "UTF-8");
    }

}
