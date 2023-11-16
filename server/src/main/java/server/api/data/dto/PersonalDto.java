package server.api.data.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.tournament.Tournament;

import java.util.Set;

public record PersonalDto(
        long id,
        String username,
        String email,
        Set<Long> teamsIds,
        Set<Long> administratedTournamentsIds,
        boolean tournamentAdmin
) {
}
