package server.facade;


import server.api.data.dto.BracketDto;
import server.api.data.response.TournamentResponseData;

import java.util.List;

public interface TournamentFacade {
    TournamentResponseData getTournament(long id);

    List<TournamentResponseData> getAllSearchedTournaments(String filter, String discipline);

    BracketDto getTournamentBracket(long id);
}
