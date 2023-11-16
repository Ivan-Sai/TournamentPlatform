package server.api.data.response;

import server.persistance.entity.tournament.Tournament;

import java.util.List;

public record AllTournamentsResponseData (
        List<TournamentResponseData> tournaments
) {
}
