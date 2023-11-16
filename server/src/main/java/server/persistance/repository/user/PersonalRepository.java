package server.persistance.repository.user;

import org.springframework.stereotype.Repository;
import server.persistance.entity.user.Personal;

import java.util.Optional;

@Repository
public interface PersonalRepository extends UserRepository<Personal> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Personal> findByEmail(String email);

    boolean existsByPassword(String password);

}
