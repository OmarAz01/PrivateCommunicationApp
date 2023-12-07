package com.privatecommunication.service;

import com.privatecommunication.dto.*;
import com.privatecommunication.entity.ChatRequestEntity;
import com.privatecommunication.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<UserDTO> findUser(Long id);

    ResponseEntity<Long> deleteUser(Long id);

    ResponseEntity<UserDTO> updateUserImage(Long id, String image);

    ResponseEntity<UserDTO> findByUsername(String username);

    ResponseEntity<UserEntity> findByEmail(String email);

    ResponseEntity<UserDTO> save(UserEntity user);

    ResponseEntity<String> changePassword(Long id, PasswordChangeDTO passwordChangeDTO);

    ResponseEntity<String> changeEmail(Long id, EmailChangeDTO emailChangeDTO);

    ResponseEntity<List<RequestDTO>> getRequests(Long id);

    ResponseEntity<List<MessageDTO>> getLastTenMessages(Long chatRoomId);

    ResponseEntity<MessageDTO> getLastMessage(Long chatRoomId);

    ResponseEntity<Long> getChatRoomId(Long id, Long id2);

    ResponseEntity setChatRoomId(Long roomId, Long user1Id, Long user2Id);

    ResponseEntity requestResponse(Long requestId, String status);

    ResponseEntity<List<PrevChatsDTO>> getPrevChats(Long id);

    ResponseEntity<List<UserDTO>> findByUsernameContaining(String username);

    ResponseEntity<?> sendRequest(Long senderId, Long receiverId);

    ResponseEntity<?> deleteChat(Long chatRoomId);
}
