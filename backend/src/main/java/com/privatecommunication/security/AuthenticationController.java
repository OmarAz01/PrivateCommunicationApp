package com.privatecommunication.security;

import com.privatecommunication.security.entity.AuthenticationRequest;
import com.privatecommunication.security.entity.AuthenticationResponse;
import com.privatecommunication.security.entity.RegisterRequest;
import com.privatecommunication.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/validate")
    public void validate(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        authService.validateToken(request, response);
    }

    @GetMapping("/logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authService.logout(request, response);
    }

}
