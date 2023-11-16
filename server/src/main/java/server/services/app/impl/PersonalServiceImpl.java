package server.services.app.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.dto.PersonalDto;
import server.api.data.dto.TeamDto;
import server.facade.TeamFacade;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.tournament.Tournament;
import server.persistance.entity.user.Personal;
import server.persistance.repository.app.ImageRepository;
import server.persistance.repository.app.TeamRepository;
import server.persistance.repository.user.PersonalRepository;
import server.services.app.PersonalService;
import server.services.app.TeamService;
import server.util.ImageUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepository personalRepository;
    private final ImageRepository imageRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;

    @Override
    public List<TeamDto> getTeamsByPersonalId(long id) {
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Team> teams = personal.getTeams();
        return teams
                .stream()
                .map(team ->
                    new TeamDto(
                            team.getId(),
                            team.getName(),
                            team.getCurPlayers(),
                            team.getTournaments().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                            team.getUsers().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                            team.getCreationDate(),
                            team.getAdmin().getId(),
                            team.getToken()))
                .toList();

    }

    @Override
    public PersonalDto findPersonalById(long id) {
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new PersonalDto(
                personal.getId(),
                personal.getUsername(),
                personal.getEmail(),
                personal.getTeams().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                personal.getTournaments().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                personal.isTournamentAdmin()
                );
    }

    @Override
    public byte[] getLogo(long id) {
        Image image = personalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found")).getAvatar();
        if (image == null) throw new EntityNotFoundException("Image not found");
        return ImageUtils.decompressImage(personalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found")).getAvatar().getBytes());
    }

    @Override
    public void updateAvatar(Long id, MultipartFile image) throws IOException {
        Image image1 = new Image();
        image1.setName(image.getOriginalFilename());
        image1.setContentType(image.getContentType());
        image1.setBytes(ImageUtils.compressImage(image.getBytes()));
        imageRepository.save(image1);
        Personal personal = personalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Personal not found"));
        Image toDel = personal.getAvatar();
        personal.setAvatar(image1);
        personalRepository.save(personal);
        if (toDel != null) {
            imageRepository.delete(toDel);
        }
    }

    @Override
    public void leaveTeam(long teamId, long userId) {
        Personal personal = personalRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        personal.getTeams().remove(teamRepository.findById(teamId).orElseThrow(()-> new EntityNotFoundException("Team not found")));
        personalRepository.save(personal);
        Team team = teamRepository.findById(teamId).orElseThrow(()-> new EntityNotFoundException("Team not found"));
        team.setCurPlayers(team.getCurPlayers() -1);
        if (team.getAdmin().getId() == userId) {
            team.getUsers().remove(personal);
            if (!team.getUsers().isEmpty()) {
                team.setAdmin(team.getUsers().get(0));
            }
            else {
                teamService.deleteTeam(teamId,userId);
            }
        }
        else {
            team.getUsers().remove(personal);
            teamRepository.save(team);
        }

    }
}

