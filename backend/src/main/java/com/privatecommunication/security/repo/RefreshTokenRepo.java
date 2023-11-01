package com.privatecommunication.security.repo;

import com.privatecommunication.security.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {

    @Query(value = "SELECT refresh_token FROM refresh_tokens WHERE user_id = :userId", nativeQuery = true)
    Optional<String> findRefreshTokenByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT last_access_token FROM refresh_tokens WHERE user_id = :userId", nativeQuery = true)
    Optional<String> findLastAccessTokenByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT user_id, refresh_token, last_access_token FROM refresh_tokens WHERE user_id = :userId", nativeQuery = true)
    Optional<RefreshTokenEntity> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM refresh_tokens WHERE last_access_token = :lastAccessToken", nativeQuery = true)
    Optional<RefreshTokenEntity> findByLastAccessToken(@Param("lastAccessToken") String lastAccessToken);

    @Transactional
    @Modifying
    @Query(value = "UPDATE refresh_tokens SET last_access_token = :newJwt WHERE user_id = :userId", nativeQuery = true)
    void updateLastAccessToken(@Param("userId") Long userId, @Param("newJwt") String newJwt);
}

