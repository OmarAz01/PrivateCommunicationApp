package com.privatecommunication.controller;

import com.privatecommunication.dto.EmailChangeDTO;
import com.privatecommunication.dto.PasswordChangeDTO;
import com.privatecommunication.dto.UserDTO;
import com.privatecommunication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
