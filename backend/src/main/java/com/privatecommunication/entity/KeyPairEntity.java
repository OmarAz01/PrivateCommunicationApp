package com.privatecommunication.entity;

import com.privatecommunication.repo.ChatRoomRepo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "key_pairs")
public class KeyPairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyPairId;
    private String publicKey;
    private String privateKey;
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public KeyPairEntity(String generatedKey, String generatedKey1, ChatRoom chatRoom) {
        this.publicKey = generatedKey;
        this.privateKey = generatedKey1;
        this.chatRoom = chatRoom;
    }

    public KeyPairEntity() {

    }
}
