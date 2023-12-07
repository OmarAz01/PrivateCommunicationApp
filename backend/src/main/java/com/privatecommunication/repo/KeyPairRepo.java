package com.privatecommunication.repo;

import com.privatecommunication.entity.KeyPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface KeyPairRepo extends JpaRepository<KeyPairEntity, Long> {

    @Query(value = "SELECT public_key FROM key_pairs WHERE chat_room_id = :chatRoomId", nativeQuery = true)
    String findByRoomId(@Param("chatRoomId") Long chatRoomId);

    @Modifying
    @Query(value = "DELETE FROM key_pairs WHERE chat_room_id = :chatRoomId", nativeQuery = true)
    void deleteByChatRoomId(Long chatRoomId);
}
