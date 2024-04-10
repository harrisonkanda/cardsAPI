package com.logicea.logiceacardsproject.security.jwt;

import com.logicea.logiceacardsproject.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProviderService {

    @Value("${security.jwt.secretKey}")
    private String signingKey;

    @Value("${security.jwt.expiration-time}")
    private Long expiration;

    public static final String EMAIL = "email";
    public static final String ROLE = "role";

    public String generateAccessToken(final UserDetails userDetails) {
        Date now = new Date();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(ROLE, userDetails.getAuthorities().stream().findFirst().get().toString());
        extraClaims.put(EMAIL, userDetails.getUsername());

        return Jwts.builder().setClaims(extraClaims)
                .setIssuer("Logicea Dev Team")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public Claims getClaimsFromJwtToken(final String token) throws BadCredentialsException, JwtExpiredTokenException {
        return parseClaims(token, signingKey);
    }

    private Claims parseClaims(final String token, final String signingKey) throws BadCredentialsException, JwtExpiredTokenException {
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException exception) {
            log.error("Invalid JWT Token", exception);
            throw new BadCredentialsException("Invalid JWT token: ", exception);
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired", expiredEx.getMessage());
            throw new JwtExpiredTokenException("JWT Token expired: " + expiredEx);
        }
    }

    public Boolean isValidateToken(final String token, final UserDetails userDetails) throws JwtExpiredTokenException {
        final Claims claims = getClaimsFromJwtToken(token);
        final String username = extractEmail(claims);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(extractExpiration(claims)));
    }

    public String extractEmail(final Claims claims) {
        return claims.get(EMAIL, String.class);
    }

    public Date extractExpiration(final Claims claims) {
        return claims.getExpiration();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(final Date tokenExpirationDate) {
        return tokenExpirationDate.before(new Date());
    }
}
