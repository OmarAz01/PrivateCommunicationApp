package com.privatecommunication.security.service;

import com.privatecommunication.security.entity.RefreshTokenEntity;
import org.springframework.http.ResponseEntity;

public interface RefreshTokenService {

    ResponseEntity<Long> deleteRefreshToken(Long userId);

    ResponseEntity<Long> logoutUser(Long userId);

    ResponseEntity<String> getRefreshToken(Long userId);

    ResponseEntity<String> getLastAccessToken(Long userId);

    ResponseEntity<RefreshTokenEntity> findByLastAccessToken(String lastAccessToken);

    ResponseEntity<RefreshTokenEntity> createRefreshToken(Long userId, String refreshToken, String lastAccessToken);

    ResponseEntity<String> updateLastAccessToken(Long userId, String newJwt);
}
