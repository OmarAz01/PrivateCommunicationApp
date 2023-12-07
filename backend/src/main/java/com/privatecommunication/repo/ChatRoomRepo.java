package com.privatecommunication.repo;

import com.privatecommunication.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT chat_room_id FROM chat_rooms WHERE (user1id = :user1Id AND user2id = :user2Id) OR (user1id = :user2Id AND user2id = :user1Id)", nativeQuery = true)
    Long getChatRoomId(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query(value = "SELECT chat_room_id FROM chat_rooms WHERE user1id = :id OR user2id = :id", nativeQuery = true)
    List<Long> getChatRoomIds(@Param("id") Long id);

    @Query(value = "SELECT * FROM chat_rooms WHERE chat_room_id = :id", nativeQuery = true)
    List<ChatRoom> getUserIds(@Param("id") Long id);

    @Query(value = "SELECT * FROM chat_rooms WHERE chat_room_id = :id", nativeQuery = true)
    ChatRoom getChatRoom(@Param("id") Long id);
}
