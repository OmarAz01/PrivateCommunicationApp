package com.privatecommunication.dto;

import com.privatecommunication.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String email;
    private String username;
    private String imageUri;

    public static UserDTO convertToDTO(UserEntity user) {
        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getImageUri()
        );
    }

    public static List<UserDTO> convertToDTO(List<UserEntity> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserEntity user : users) {
            UserDTO userDTO = new UserDTO(
                    user.getUserId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getImageUri()
            );
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }
}

