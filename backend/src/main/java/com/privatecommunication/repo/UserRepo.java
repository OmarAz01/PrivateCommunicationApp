package com.privatecommunication.repo;

import com.privatecommunication.dto.UserDTO;
import com.privatecommunication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query(value = "SELECT user_id, email, username, image_uri FROM users WHERE username = :username", nativeQuery = true)
    Optional<UserDTO> findByUsername(@Param("username") String username);

    Optional<UserEntity> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "delete from users where user_id = :userId", nativeQuery = true)
    void deleteById(@Param("userId") Long userId);

}
