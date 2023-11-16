package server.services.app;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.request.TournamentData;
import server.api.data.response.TournamentCreationData;
import server.persistance.entity.tournament.Tournament;

import java.io.IOException;
import java.util.List;


public interface TournamentService {

    TournamentCreationData createTournament(TournamentData data);

    Tournament getTournament(long id);

    void updateTournamentImage(long id, MultipartFile image) throws IOException;

    byte[] getLogo(long id);

    void deleteTournament(long id);

    void cancelTournament(long id);

    List<Tournament> getTournaments();

    List<Tournament> getTournamentsByFilter(String filter);

    void addTeam(long tournamentId, long teamId);

    void teamWon(long bracketId,long stageId, long matchId,long teamId);
}
