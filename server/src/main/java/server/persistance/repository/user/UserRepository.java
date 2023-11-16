package server.persistance.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository<U extends User> extends JpaRepository<U,Long> {
    Optional<U> findByUsername(String username);
}
