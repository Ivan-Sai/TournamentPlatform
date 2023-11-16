package server.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.config.security.JwtService;
import server.persistance.entity.token.Token;
import server.persistance.entity.user.Admin;
import server.persistance.entity.user.Personal;
import server.persistance.repository.token.TokenRepository;
import server.persistance.repository.user.AdminRepository;
import server.persistance.type.security.TokenType;

@Service
@RequiredArgsConstructor
public class AdminGeneratorService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

//    @Scheduled(cron = "*/5 * * * * *")
    public void generateAdmin() {
        System.out.println("start");
        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setEmail("admin1@gmail.com");
        admin.setPassword(passwordEncoder.encode("lEpwBJfqK9"));
        adminRepository.save(admin);
        String token = jwtService.generateToken(admin);
        saveToken(token, admin);
        System.out.println("end");
    }

    private void saveToken(String accessToken, Admin admin) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setTokenType(TokenType.BEARER);
        token.setUser(admin);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }
}
