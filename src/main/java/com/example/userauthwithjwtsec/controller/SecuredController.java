package com.example.userauthwithjwtsec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secured-controller")
public class SecuredController {

    @GetMapping
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome to Code With DOUGG");
    }
}
