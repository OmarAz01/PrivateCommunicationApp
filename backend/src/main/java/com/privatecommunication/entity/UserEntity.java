package com.privatecommunication.entity;

import com.privatecommunication.dto.UserDTO;
import com.privatecommunication.security.entity.RefreshTokenEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(length = 10)
    private String username;
    @Column(length = 50)
    private String email;
    private String password;
    private String imageUri;
    @Enumerated(EnumType.STRING)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private RefreshTokenEntity refreshToken;

    public UserDTO userDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        userDTO.setImageUri(imageUri);
        return userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
