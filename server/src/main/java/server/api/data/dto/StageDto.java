package server.api.data.dto;

import server.persistance.entity.bracket.Match;

import java.util.List;

public record StageDto(
        long stageId,
        List<MatchDto> matches
) {
}
