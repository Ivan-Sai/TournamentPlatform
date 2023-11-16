package server.api.data.dto;

import java.util.List;

public record MatchDto(
        long matchId,
        TeamDto team1,
        TeamDto team2,
        long winnerId
) {
}
