package server.persistance.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.persistance.entity.bracket.Bracket;
import server.persistance.entity.bracket.Stage;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
}
