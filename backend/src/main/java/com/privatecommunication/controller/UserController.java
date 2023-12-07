package com.privatecommunication.controller;

import com.privatecommunication.dto.*;
import com.privatecommunication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        return userService.findUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<UserDTO> updateUserImage(@PathVariable("id") Long id, @RequestBody String image) {
        return userService.updateUserImage(id, image);
    }

    @PostMapping("/{id}/changePassword")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordChangeDTO passwordChangeDTO) {
        return userService.changePassword(id, passwordChangeDTO);
    }

    @PostMapping("/{id}/changeEmail")
    public ResponseEntity<String> changeEmail(@PathVariable("id") Long id, @RequestBody EmailChangeDTO emailChangeDTO) {
        return userService.changeEmail(id, emailChangeDTO);
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<?> getRequests(@PathVariable("id") Long id) {
        return userService.getRequests(id);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getLastTenMessages(@PathVariable("roomId") Long roomId) {
        return userService.getLastTenMessages(roomId);
    }

    @GetMapping("/{roomId}/lastMessage")
    public ResponseEntity<?> getLastMessage(@PathVariable("roomId") Long roomId) {
        return userService.getLastMessage(roomId);
    }

    @GetMapping("/{id}/{id2}")
    public ResponseEntity<Long> getChatRoom(@PathVariable("id") Long id, @PathVariable("id2") Long id2) {
        return userService.getChatRoomId(id, id2);
    }

    @PostMapping("/roomId")
    public ResponseEntity<Long> setChatRoomId(@RequestBody Long roomId, @RequestBody Long user1Id, @RequestBody Long user2Id) {
        return userService.setChatRoomId(roomId, user1Id, user2Id);
    }

    @PostMapping("/requestResponse")
    public ResponseEntity<?> requestResponse(@RequestBody RequestResponseDTO requestResponseDTO) {
        return userService.requestResponse(requestResponseDTO.getRequestId(), requestResponseDTO.getStatus());
    }

    @GetMapping("/{id}/prevChats")
    public ResponseEntity<List<PrevChatsDTO>> getPrevChats(@PathVariable("id") Long id) {
        return userService.getPrevChats(id);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<UserDTO>> findByUsername(@PathVariable("username") String username) {
        return userService.findByUsernameContaining(username);
    }

    @PostMapping("/request/{senderId}/{receiverId}")
    public ResponseEntity<?> sendRequest(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        return userService.sendRequest(senderId, receiverId);
    }

    @DeleteMapping("/chat/{chatRoomId}")
    public ResponseEntity<?> deleteChat(@PathVariable("chatRoomId") Long chatRoomId) {
        return userService.deleteChat(chatRoomId);
    }

}
