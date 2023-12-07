package com.privatecommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long senderId;
    private Long recipientId;
    private String content;
    private Long chatRoomId;

    public MessageDTO(Long userId, Long userId1, String content) {
        this.senderId = userId;
        this.recipientId = userId1;
        this.content = content;
    }
}
