package server.persistance.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.persistance.entity.user.Admin;
import server.persistance.entity.user.Personal;

import java.util.Optional;

@Repository
public interface AdminRepository extends UserRepository<Admin> {
    Optional<Admin> findByEmail(String email);
}
