package com.mai.flower_shop.security.jwt;

import com.mai.flower_shop.security.user.ShopUserDetails;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;
    @Value("${auth.token.refreshexpirationInMils}")
    private int refreshExpirationTime;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String generateTokenForUser (Authentication authentication, boolean isFresh){
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        String jwtId = UUID.randomUUID().toString();
        Long expirationTimeToken = Long.valueOf(isFresh ? expirationTime : refreshExpirationTime);
        return Jwts.builder()
                .setId(jwtId)
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
               .setExpiration(new Date((new Date()).getTime() +expirationTimeToken))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    // create key by secretKey
    private Key key (){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public String getUsernameFromToken( String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();

    }
    public String getIDFromToken( String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getId();

    }
    // check token true or false
    public boolean validateToken (String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }


}
