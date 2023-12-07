package com.privatecommunication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    private Long chatRoomId;
    private Long user1Id;
    private Long user2Id;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyPairEntity> keyPairs;

    public ChatRoom(Long roomId, Long user1Id, Long user2Id) {
        this.chatRoomId = roomId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}
