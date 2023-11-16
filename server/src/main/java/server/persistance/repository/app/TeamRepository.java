package server.persistance.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.persistance.entity.team.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
