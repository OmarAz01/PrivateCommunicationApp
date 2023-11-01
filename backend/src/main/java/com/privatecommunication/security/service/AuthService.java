package com.privatecommunication.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatecommunication.dto.UserDTO;
import com.privatecommunication.entity.UserEntity;
import com.privatecommunication.security.entity.AuthenticationRequest;
import com.privatecommunication.security.entity.AuthenticationResponse;
import com.privatecommunication.security.entity.RefreshTokenEntity;
import com.privatecommunication.security.entity.RegisterRequest;
import com.privatecommunication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;


    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        if (userService.findByUsername(request.getUsername()).getBody() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder()
                    .error("Username already exists").build());
        }

        if (userService.findByEmail(request.getEmail()).getBody() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthenticationResponse.builder()
                    .error("Email already exists").build());
        }

        // Creating a new user
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Saving the user
        try {
            UserDTO savedUser = userService.save(user).getBody();
            if (savedUser == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthenticationResponse
                        .builder().error("User registration failed").build());
            }
            // Create refresh and access tokens
            Long userId = savedUser.getUserId();
            String jwt = jwtService.generateToken(userId);
            String refreshToken = jwtService.generateRefreshToken(userId);
            refreshTokenService.createRefreshToken(savedUser.getUserId(), refreshToken, jwt);
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder()
                    .userId(userId).jwt(jwt).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthenticationResponse
                    .builder().error("User registration failed").build());
        }
    }


    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        UserEntity user = userService.findByEmail(request.getEmail()).getBody();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthenticationResponse
                    .builder().error("User not found").build());
        } else {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword(), Collections.emptyList()));
        }
        Long userId = user.getUserId();
        // Delete old refresh token
        refreshTokenService.deleteRefreshToken(userId);
        // Create new refresh and access tokens
        String jwt = jwtService.generateToken(userId);
        String refreshToken = jwtService.generateRefreshToken(userId);
        refreshTokenService.createRefreshToken(user.getUserId(), refreshToken, jwt);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword(), Collections.emptyList()));
        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder()
                .userId(user.getUserId()).jwt(jwt).build());
    }

    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        Object currentPrincipal = authContext.getPrincipal();
        Long userId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No token found");
            return;
        }

        if (currentPrincipal instanceof UserEntity) {
            UserEntity currentUser = (UserEntity) currentPrincipal;
            userId = currentUser.getUserId();
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Not logged in");
            return;
        }

        String jwt = authHeader.substring(7);

        if (!jwtService.isTokenValid(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        if (jwtService.isTokenExpired(jwt)) {
            RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByLastAccessToken(jwt).getBody();
            if (refreshTokenEntity == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token not found");
                return;
            }
            if (jwtService.isTokenExpired(refreshTokenEntity.getRefreshToken())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token expired");
                return;
            }

            // If the refresh token is valid and not expired, create a new access token and update database with it
            String newJwt = jwtService.generateToken(userId);
            refreshTokenService.updateLastAccessToken(userId, newJwt);
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .userId(userId).jwt(newJwt).build();
            new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
        } else {
            // Return the current token if it is valid and not expired
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .userId(userId).jwt(jwt).build();
            new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String jwt = authHeader.substring(7);
        Long id = Long.valueOf(jwtService.getUserId(jwt));
        refreshTokenService.deleteRefreshToken(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
