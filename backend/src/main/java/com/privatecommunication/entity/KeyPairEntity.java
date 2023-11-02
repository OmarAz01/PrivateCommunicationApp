package com.privatecommunication.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "key_pairs", uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
public class KeyPairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyPairId;
    private String publicKey;
    private String privateKey;
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private UserEntity user1;
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private UserEntity user2;
}
