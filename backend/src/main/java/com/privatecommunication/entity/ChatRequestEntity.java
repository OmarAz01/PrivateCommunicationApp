package com.privatecommunication.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "chat_requests")
public class ChatRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserEntity recipient;
    private Boolean isAccepted;
}
