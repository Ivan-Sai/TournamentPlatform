package server.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import server.persistance.entity.user.User;

import java.util.List;

public interface JwtService {

    String generateToken(User user);
    String generateRefreshToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenExpired(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
