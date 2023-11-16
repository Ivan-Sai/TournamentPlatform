package server.persistance.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.persistance.entity.bracket.Bracket;
import server.persistance.entity.bracket.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
