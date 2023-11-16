package server.services.security.impl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.api.data.request.LoginData;
import server.api.data.request.RegisterData;
import server.api.data.response.AuthenticationData;
import server.config.security.JwtService;

import server.exception.UserExistsException;
import server.persistance.entity.token.Token;
import server.persistance.entity.user.Personal;
import server.persistance.entity.user.User;
import server.persistance.repository.token.TokenRepository;
import server.persistance.repository.user.AdminRepository;
import server.persistance.repository.user.PersonalRepository;
import server.persistance.type.security.TokenType;
import server.services.security.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PersonalRepository personalRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;


    @Override
    public AuthenticationData register(RegisterData data) {
        if (personalRepository.existsByEmail(data.getEmail())) {
            throw new UserExistsException("User with email already exists");
        }
        if (personalRepository.existsByUsername(data.getUsername())) {
            throw new UserExistsException("User with username already exists");
        }
        Personal personal = new Personal();
        personal.setUsername(data.getUsername());
        personal.setEmail(data.getEmail());
        personal.setPassword(passwordEncoder.encode(data.getPassword()));
        personalRepository.save(personal);
        String token = jwtService.generateToken(personal);
        saveToken(token, personal);
        return new AuthenticationData(token);
    }

    @Override
    public AuthenticationData login(LoginData data) {
        User user = new User();
        if (personalRepository.findByEmail(data.getEmail()).isPresent()) {
            user = personalRepository.findByEmail(data.getEmail()).get();
        }
        else {
            user = adminRepository.findByEmail(data.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException("Incorrect password");
        }
        Token token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
        String accessToken = token.getToken();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), data.getPassword())
        );
        try {
            jwtService.isTokenExpired(accessToken);
        } catch (ExpiredJwtException e) {
            tokenRepository.deleteById(token.getId());
            accessToken = jwtService.generateToken(user);
            saveToken(accessToken, user);
        }

        return new AuthenticationData(accessToken);
    }


    private void saveToken(String accessToken, User user) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setTokenType(TokenType.BEARER);
        token.setUser(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }
}