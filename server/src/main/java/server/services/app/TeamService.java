package server.services.app;

import org.springframework.web.multipart.MultipartFile;
import server.api.data.dto.PersonalDto;
import server.persistance.entity.team.Team;

import java.io.IOException;
import java.util.List;

public interface TeamService {
    byte[] getLogo(long id);

    long createTeam(String name, long ownerId, MultipartFile image) throws IOException;

    Team findById(long id);

    void updateLogo(Long id, MultipartFile file) throws IOException;

    List<PersonalDto> getPersonalsByTeam(long id);

    void addUser(long teamId, long userId, String teamUUID);

    void deleteTeam(long teamId, long userId);
}
