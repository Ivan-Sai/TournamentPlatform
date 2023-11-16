package server.facade;

import server.api.data.dto.TeamDto;
import server.persistance.entity.team.Team;

import java.util.List;

public interface TeamFacade {
    TeamDto findById(long id);
}
