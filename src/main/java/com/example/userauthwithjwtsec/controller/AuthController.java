package com.example.userauthwithjwtsec.controller;

import com.example.userauthwithjwtsec.dto.AuthenticationRequest;
import com.example.userauthwithjwtsec.dto.AuthenticationResponse;
import com.example.userauthwithjwtsec.dto.RegisterRequest;
import com.example.userauthwithjwtsec.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService authenticateService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody RegisterRequest registerRequest){

        return ResponseEntity.ok(authenticateService.signup(registerRequest));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody AuthenticationRequest authenticationRequest){

        return ResponseEntity.ok(authenticateService.signIn(authenticationRequest));

    }

}
