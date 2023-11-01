package com.privatecommunication.security.service;

import com.privatecommunication.security.entity.RefreshTokenEntity;
import com.privatecommunication.security.repo.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${signin.key}")
    private String signInKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    private final RefreshTokenRepo refreshTokenRepo;

    public JwtService(RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }

    public String getUserId(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(jwt);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            Claims claim = e.getClaims();
            return claimsResolver.apply(claim);
        }

    }

    public String generateToken(Long userId) {
        return generateToken(new HashMap<>(), userId);
    }

    public String generateRefreshToken(Long userId) {
        return generateRefreshToken(new HashMap<>(), userId);
    }


    public String generateToken(Map<String, Object> extraClaims, Long userId) {
        return buildToken(extraClaims, userId, jwtExpiration);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, Long userId) {
        return buildToken(extraClaims, userId, jwtRefreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, Long userId, Long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt) {
        Long userId = Long.parseLong(getUserId(jwt));
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepo.findByLastAccessToken(jwt);
        if (refreshTokenEntity.isPresent() && refreshTokenEntity.get().getUser().getUserId().equals(userId)) {
            return true;
        }
        return false;
    }

    public boolean isTokenExpired(String jwt) {
        if (extractExp(jwt).before(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    private Date extractExp(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }


    public Claims extractAllClaims(String jwt) {
        try {
            return Jwts
                    .parserBuilder().setSigningKey(getSignInKey()).build()
                    .parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signInKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}