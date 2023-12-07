package com.privatecommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long requestId;
    private Long senderId;
    private String senderUsername;
    private Long recipientId;
    private String imageUri;

}
