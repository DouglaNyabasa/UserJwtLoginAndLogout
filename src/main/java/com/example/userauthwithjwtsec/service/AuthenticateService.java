package com.example.userauthwithjwtsec.service;

import com.example.userauthwithjwtsec.Utility.JwtService;
import com.example.userauthwithjwtsec.dto.AuthenticationRequest;
import com.example.userauthwithjwtsec.dto.AuthenticationResponse;
import com.example.userauthwithjwtsec.dto.RegisterRequest;
import com.example.userauthwithjwtsec.model.Role;
import com.example.userauthwithjwtsec.model.Token;
import com.example.userauthwithjwtsec.model.TokenType;
import com.example.userauthwithjwtsec.model.User;
import com.example.userauthwithjwtsec.repository.TokenRepository;
import com.example.userauthwithjwtsec.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse signup(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        revokeAllUser(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token= Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUser(User user){
        var validToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validToken.isEmpty())
            return;
        validToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);

        });
        tokenRepository.saveAll(validToken);
    }

    public AuthenticationResponse signIn(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
