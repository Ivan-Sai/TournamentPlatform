package server.api.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder

public record TournamentResponseData(
        String name,
        String discipline,
        int maxTeams,
        int minTeams,
        int currTeams,
        LocalDateTime creationDate,
        LocalDateTime startDate,
        long adminId,
        String tournamentType,
        boolean isStarted,
        boolean isCanceled,
        long id,
        long bracketId,
        long winnerId
) {
}
