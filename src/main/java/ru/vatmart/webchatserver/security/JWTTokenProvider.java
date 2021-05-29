package ru.vatmart.webchatserver.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.vatmart.webchatserver.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    public static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);
    @Value("$(jwt.secret)")
    private String secret;
    @Value("${jwt.ref-expiration-ms}")
    private long refExp;
    @Value("${jwt.acc-expiration-ms}")
    private long accExp;

    enum JwtTokenType {
        refresh,
        access
    }

    public String generateToken(Authentication authentication, JwtTokenType type) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + (type.equals(JwtTokenType.access) ? accExp : refExp));

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("nickname", user.getNickname());
        claimsMap.put("jwt-token-type", type.toString());


        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }

    public boolean validateToken(String token, JwtTokenType type) {
        try {
            if (
            !Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("jwt-token-type")
                    .equals(type.toString())
            ) {
                logger.error("Invalid JWT type signature");
                return false;
            }
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }

}
