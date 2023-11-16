package server.api.data.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TeamDto(
        long id,
        String name,
        int currPlayers,
        Set<Long> tournamentsId,
        Set<Long> usersId,
        LocalDateTime creationDate,
        long adminId,
        UUID token

) {
}
