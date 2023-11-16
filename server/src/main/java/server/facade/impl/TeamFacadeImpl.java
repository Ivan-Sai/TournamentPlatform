package server.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.api.data.dto.TeamDto;
import server.facade.TeamFacade;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.team.Team;
import server.services.app.TeamService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamFacadeImpl implements TeamFacade {

    private final TeamService teamService;

    @Override
    public TeamDto findById(long id) {
        Team team = teamService.findById(id);
        return new TeamDto(
                team.getId(),
                team.getName(),
                team.getCurPlayers(),
                team.getTournaments().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                team.getUsers().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                team.getCreationDate(),
                team.getAdmin().getId(),
                team.getToken()
        );
    }
}
