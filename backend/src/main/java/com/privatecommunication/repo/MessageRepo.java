package com.privatecommunication.repo;

import com.privatecommunication.dto.MessageDTO;
import com.privatecommunication.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT * FROM messages WHERE chat_room_id = :chatRoomId ORDER BY message_id DESC LIMIT 1", nativeQuery = true)
    Optional<MessageEntity> getLastMessage(@Param("chatRoomId") Long chatRoomId);

    @Query(value = "SELECT * FROM messages WHERE chat_room_id = :chatRoomId ORDER BY message_id DESC LIMIT 10", nativeQuery = true)
    Optional<List<MessageEntity>> getLastTenMessages(@Param("chatRoomId") Long chatRoomId);

    @Modifying
    @Query(value = "DELETE FROM messages WHERE chat_room_id = :chatRoomId", nativeQuery = true)
    void deleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
