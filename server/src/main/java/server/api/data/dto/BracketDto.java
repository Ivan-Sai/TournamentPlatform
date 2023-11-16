package server.api.data.dto;

import java.util.List;

public record BracketDto(
        long bracketId,
        List<StageDto> stages
) {
}
