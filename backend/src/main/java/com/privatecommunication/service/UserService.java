package com.privatecommunication.service;

import com.privatecommunication.dto.EmailChangeDTO;
import com.privatecommunication.dto.PasswordChangeDTO;
import com.privatecommunication.dto.UserDTO;
import com.privatecommunication.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserDTO> findUser(Long id);

    ResponseEntity<Long> deleteUser(Long id);

    ResponseEntity<UserDTO> updateUserImage(Long id, String image);

    ResponseEntity<UserDTO> findByUsername(String username);

    ResponseEntity<UserEntity> findByEmail(String email);

    ResponseEntity<UserDTO> save(UserEntity user);

    ResponseEntity<String> changePassword(Long id, PasswordChangeDTO passwordChangeDTO);

    ResponseEntity<String> changeEmail(Long id, EmailChangeDTO emailChangeDTO);

}
