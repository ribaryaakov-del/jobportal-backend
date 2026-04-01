package com.pluralsight.jobportal.controller;

import com.pluralsight.jobportal.model.AuthRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import com.pluralsight.jobportal.util.JwtUtil;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        log.info("Received login request for user: {}, {}", request.getEmail(), request.getPassword());
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            //login with email as username
            String email = authentication.getName();

            String token = jwtUtil.generateToken(email);

            Map<String, String> body = Map.of("token", token);
            log.info("Generated JWT Token: {}", token);
            return ResponseEntity.ok()
                    .body(body);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }


    }

    @GetMapping("/details")
    public Map<String, Object> getUserDetails(Authentication authentication) {
        return Map.of(
                "email", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }



}
