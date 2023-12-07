package com.privatecommunication.websocket;

import com.privatecommunication.dto.MessageDTO;
import com.privatecommunication.entity.MessageEntity;
import com.privatecommunication.entity.UserEntity;
import com.privatecommunication.helper.Encryption;
import com.privatecommunication.repo.ChatRoomRepo;
import com.privatecommunication.repo.KeyPairRepo;
import com.privatecommunication.repo.MessageRepo;
import com.privatecommunication.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserRepo useRepo;

    private final KeyPairRepo keyPairRepo;
    private final ChatRoomRepo chatRoomRepo;

    public WebSocketController(KeyPairRepo keyPairRepo, ChatRoomRepo chatRoomRepo) {
        this.keyPairRepo = keyPairRepo;
        this.chatRoomRepo = chatRoomRepo;
    }

    @Transactional
    @MessageMapping("/message")
    @SendTo("/topic/private")
    public MessageDTO getMsg(MessageDTO messageDTO) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Message received: " + messageDTO.getContent());
        try {
            MessageEntity messageEntity = new MessageEntity();
            System.out.println("Chat room id: " + messageDTO.getChatRoomId());
            String secretKey = keyPairRepo.findByRoomId(messageDTO.getChatRoomId());
            System.out.println("Secret key: " + secretKey);
            String encryptedMessage = Encryption.encrypt(messageDTO.getContent(), secretKey);
            messageEntity.setContent(encryptedMessage);
            Optional<UserEntity> sender = useRepo.findByUserId(messageDTO.getSenderId());
            messageEntity.setSender(sender.get());
            Optional<UserEntity> recipient = useRepo.findByUserId(messageDTO.getRecipientId());
            messageEntity.setRecipient(recipient.get());
            messageEntity.setChatRoom(chatRoomRepo.findById(messageDTO.getChatRoomId()).get());
            messageRepo.save(messageEntity);
            System.out.println("Message sent: " + messageDTO.getContent());
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return messageDTO;
    }
}
