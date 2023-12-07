package com.privatecommunication.service;

import com.privatecommunication.dto.*;
import com.privatecommunication.entity.*;
import com.privatecommunication.helper.Encryption;
import com.privatecommunication.helper.KeyGeneratorUtil;
import com.privatecommunication.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ChatRequestsRepo chatRequestsRepo;
    private final MessageRepo messageRepo;
    private final ChatRoomRepo chatRoomRepo;
    private final KeyPairRepo keyPairRepo;

    @Override
    public ResponseEntity<UserDTO> findUser(Long id) {
        Optional<UserEntity> user = userRepo.findByUserId(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        UserDTO userDTO = UserDTO.convertToDTO(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Override
    public ResponseEntity<Long> deleteUser(Long id) {
        Optional<UserEntity> user = userRepo.findByUserId(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            userRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUserImage(Long id, String image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity) {
            if (!((UserEntity) principal).getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<UserEntity> existingUser = userRepo.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (existingUser.get().getImageUri() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        existingUser.get().setImageUri(image);
        try {
            UserEntity userRes = userRepo.save(existingUser.get());
            UserDTO userDTO = UserDTO.convertToDTO(userRes);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<UserDTO> findByUsername(String username) {
        Optional<UserDTO> user = userRepo.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @Override
    public ResponseEntity<UserEntity> findByEmail(String email) {
        Optional<UserEntity> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());

    }

    @Override
    public ResponseEntity<UserDTO> save(UserEntity user) {
        try {
            UserEntity newUser = userRepo.save(user);
            UserDTO userDTO = UserDTO.convertToDTO(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<String> changePassword(Long id, PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity) {
            if (!((UserEntity) principal).getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<UserEntity> existingUser = userRepo.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String oldPassword = passwordChangeDTO.getOldPassword();
        String newPassword = passwordChangeDTO.getNewPassword();
        System.out.println(passwordEncoder.encode(oldPassword));
        System.out.println(existingUser.get().getPassword());
        if (!passwordEncoder.matches(oldPassword, existingUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }
        existingUser.get().setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepo.save(existingUser.get());
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> changeEmail(Long id, EmailChangeDTO emailChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity) {
            if (!((UserEntity) principal).getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<UserEntity> existingUser = userRepo.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String password = emailChangeDTO.getPassword();
        String newEmail = emailChangeDTO.getEmail();
        if (!passwordEncoder.matches(password, existingUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is incorrect");
        }
        if (userRepo.findByEmail(newEmail).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        existingUser.get().setEmail(newEmail);
        try {
            userRepo.save(existingUser.get());
            return ResponseEntity.status(HttpStatus.OK).body("Email changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<List<RequestDTO>> getRequests(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<ChatRequestEntity> requests = chatRequestsRepo.findRequestForUser(id);
        List<RequestDTO> requestDTOs = new ArrayList<>();
        for (ChatRequestEntity request : requests) {
            RequestDTO requestDTO = new RequestDTO(request.getRequestId(), request.getSender().getUserId(), request.getSender().getUsername(), request.getRecipient().getUserId(), request.getSender().getImageUri());
            requestDTOs.add(requestDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(requestDTOs);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getLastTenMessages(Long chatRoomId) {
        Optional<List<MessageEntity>> messages = messageRepo.getLastTenMessages(chatRoomId);
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (MessageEntity message : messages.get()) {
            String key = keyPairRepo.findByRoomId(chatRoomId);
            String decryptedMessage = Encryption.decrypt(message.getContent(), key);
            MessageDTO messageDTO = new MessageDTO(message.getSender().getUserId(), message.getRecipient().getUserId(), decryptedMessage, message.getChatRoom().getChatRoomId());
            messageDTOs.add(messageDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(messageDTOs);
    }

    @Override
    public ResponseEntity<MessageDTO> getLastMessage(Long chatRoomId) {
        Optional<MessageEntity> message = messageRepo.getLastMessage(chatRoomId);
        if (message.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        MessageDTO messageDTO = new MessageDTO(message.get().getSender().getUserId(), message.get().getRecipient().getUserId(), message.get().getContent(), message.get().getChatRoom().getChatRoomId());
        return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
    }

    @Override
    public ResponseEntity<Long> getChatRoomId(Long id, Long id2) {
        Long chatRoomId = chatRoomRepo.getChatRoomId(id, id2);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomId);
    }

    @Override
    public ResponseEntity<Long> setChatRoomId(Long roomId, Long user1Id, Long user2Id) {
        ChatRoom chatRoom = new ChatRoom(roomId, user1Id, user2Id);
        chatRoomRepo.save(chatRoom);
        return ResponseEntity.status(HttpStatus.OK).body(roomId);
    }

    @Override
    public ResponseEntity requestResponse(Long requestId, String status) {
        Optional<ChatRequestEntity> request = chatRequestsRepo.findById(requestId);
        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (status.equals("Accepted")) {
            Long roomId = Math.abs(UUID.randomUUID().getLeastSignificantBits() % 10_000_000_000_000L);

            ChatRoom chatRoom = new ChatRoom(roomId, request.get().getSender().getUserId(), request.get().getRecipient().getUserId());
            chatRoomRepo.save(chatRoom);
            chatRequestsRepo.delete(request.get());
            String generatedKey = KeyGeneratorUtil.generateRandomKey();
            keyPairRepo.save(new KeyPairEntity(generatedKey, generatedKey, chatRoom));
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else if (status.equals("Rejected")) {
            chatRequestsRepo.delete(request.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<PrevChatsDTO>> getPrevChats(Long id) {
        List<Long> chatRoomIds = chatRoomRepo.getChatRoomIds(id);
        List<PrevChatsDTO> prevChatsDTOs = new ArrayList<>();
        if (chatRoomIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        for (Long chatRoomId : chatRoomIds) {
            if (messageRepo.getLastMessage(chatRoomId).isEmpty()) {
                List<ChatRoom> chatRooms = chatRoomRepo.getUserIds(chatRoomId);
                UserEntity user = new UserEntity();
                for (ChatRoom chatRoom : chatRooms) {
                    if (chatRoom.getUser1Id() != id) {
                        user = userRepo.findById(chatRoom.getUser1Id()).get();
                    } else {
                        user = userRepo.findById(chatRoom.getUser2Id()).get();
                    }
                }
                prevChatsDTOs.add(new PrevChatsDTO(user.getUserId(), user.getUsername(), "", user.getImageUri()));
            }
            else {
                MessageEntity message = messageRepo.getLastMessage(chatRoomId).get();
                Long recipientId = message.getRecipient().getUserId() == id ? message.getSender().getUserId() : message.getRecipient().getUserId();
                String recipientUsername = message.getRecipient().getUserId() == id ? message.getSender().getUsername() : message.getRecipient().getUsername();
                String key = keyPairRepo.findByRoomId(chatRoomId);
                String lastMessage = Encryption.decrypt(message.getContent(), key);
                prevChatsDTOs.add(new PrevChatsDTO(recipientId, recipientUsername, lastMessage, message.getRecipient().getImageUri()));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(prevChatsDTOs);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findByUsernameContaining(String username) {
        List<UserEntity> users = userRepo.findByUsernameContaining(username);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserEntity user : users) {
            userDTOs.add(UserDTO.convertToDTO(user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

    @Override
    public ResponseEntity<?> sendRequest(Long senderId, Long receiverId) {
        Optional<UserEntity> sender = userRepo.findById(senderId);
        Optional<UserEntity> receiver = userRepo.findById(receiverId);
        if (sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (sender.get().getUserId().equals(receiver.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot send a request to yourself");
        }
        if (chatRequestsRepo.findBySenderAndRecipient(sender.get().getUserId(), receiver.get().getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already sent a request to this user");
        }
        if (chatRequestsRepo.findBySenderAndRecipient(receiver.get().getUserId(), sender.get().getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user has already sent you a request");
        }
        ChatRequestEntity request = new ChatRequestEntity(sender.get(), receiver.get(), "Pending");
        chatRequestsRepo.save(request);
        return ResponseEntity.status(HttpStatus.OK).body("Request sent successfully");
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteChat(Long chatRoomId) {
        System.out.println(chatRoomId);
        try {
            ChatRoom chatRoom = chatRoomRepo.getChatRoom(chatRoomId);
            if (chatRoom != null) {
                System.out.println(chatRoom.getChatRoomId());
                messageRepo.deleteByChatRoomId(chatRoom.getChatRoomId());
                keyPairRepo.deleteByChatRoomId(chatRoom.getChatRoomId());
                chatRoomRepo.deleteById(chatRoom.getChatRoomId());
                return ResponseEntity.status(HttpStatus.OK).body("Chat deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat room not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting chat");
        }
    }

}
