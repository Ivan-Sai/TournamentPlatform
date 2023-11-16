package server.api.data.response;

import java.util.List;

public record BracketResponseData(
        long bracketId,
        List<Long> stagesIds,
        List<Long> matches
) {
}
