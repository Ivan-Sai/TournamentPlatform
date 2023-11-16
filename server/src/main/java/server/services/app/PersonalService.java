package server.services.app;

import org.springframework.web.multipart.MultipartFile;
import server.api.data.dto.PersonalDto;
import server.api.data.dto.TeamDto;
import server.persistance.entity.team.Team;

import java.io.IOException;
import java.util.List;

public interface PersonalService {
    List<TeamDto> getTeamsByPersonalId(long id);

    PersonalDto findPersonalById(long id);

    byte[] getLogo(long id);

    void updateAvatar(Long id, MultipartFile file) throws IOException;

    void leaveTeam(long teamId, long userId);
}
