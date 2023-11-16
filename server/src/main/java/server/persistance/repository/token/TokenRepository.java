package server.persistance.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import server.persistance.entity.token.Token;
import server.persistance.entity.user.Personal;
import server.persistance.entity.user.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByUser(User user);

    Optional<Token> findByToken(String jwt);
}
