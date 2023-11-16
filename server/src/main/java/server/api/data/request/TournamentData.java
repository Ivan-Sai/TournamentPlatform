package server.api.data.request;

import java.time.LocalDateTime;

public record TournamentData(
        String name,
        String discipline,
        int maxTeams,
        int minTeams,
        LocalDateTime creationDate,
        LocalDateTime startDate,
        long adminId,
        String tournamentType

) {
}
