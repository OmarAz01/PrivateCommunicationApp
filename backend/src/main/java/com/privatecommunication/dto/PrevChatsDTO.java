package com.privatecommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrevChatsDTO {
    private Long recipientId;
    private String recipientUsername;
    private String lastMessage;
    private String imageUri;
}
