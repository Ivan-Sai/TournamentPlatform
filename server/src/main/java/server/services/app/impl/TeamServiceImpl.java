package server.services.app.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.dto.PersonalDto;
import server.exception.FullTeamException;
import server.exception.NotValidDataException;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.user.Personal;
import server.persistance.repository.app.ImageRepository;
import server.persistance.repository.app.TeamRepository;
import server.persistance.repository.app.TournamentRepository;
import server.persistance.repository.user.PersonalRepository;
import server.services.app.TeamService;
import server.util.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PersonalRepository personalRepository;
    private final ImageRepository imageRepository;
    private final TournamentRepository tournamentRepository;


    @Override
    public byte[] getLogo(long id) {
        Image image = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found")).getLogo();
        if (image == null) throw new EntityNotFoundException("Image not found");
        return ImageUtils.decompressImage(teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found")).getLogo().getBytes());
    }

    @Override
    public long createTeam(String name, long ownerId, MultipartFile uploadImage) throws IOException {
        Team team = new Team();
        team.setName(name);
        team.setCurPlayers(1);
        team.setCreationDate(LocalDateTime.now());
        team.setToken(UUID.randomUUID());
        List<Personal> users = team.getUsers();
        if (users == null) users = new ArrayList<>();
        users.add(personalRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("User not found")));
        team.setUsers(users);
        team.setAdmin(personalRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("User not found")));
        Image image = new Image();
        image.setName(uploadImage.getOriginalFilename());
        image.setContentType(uploadImage.getContentType());
        image.setBytes(ImageUtils.compressImage(uploadImage.getBytes()));
        imageRepository.save(image);
        team.setLogo(image);
        teamRepository.save(team);
        return team.getId();
    }

    @Override
    public Team findById(long id) {
        return teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
    }

    @Override
    public void updateLogo(Long id, MultipartFile image) throws IOException {
        Image image1 = new Image();
        image1.setName(image.getOriginalFilename());
        image1.setContentType(image.getContentType());
        image1.setBytes(ImageUtils.compressImage(image.getBytes()));
        imageRepository.save(image1);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        Image toDel = team.getLogo();
        team.setLogo(image1);
        teamRepository.save(team);
        if (toDel!= null) imageRepository.delete(toDel);
    }

    @Override
    public List<PersonalDto> getPersonalsByTeam(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"))
                .getUsers()
                .stream()
                .map(user -> new PersonalDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getTeams().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                        user.getTournaments().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                        user.isTournamentAdmin()
                ))
                .toList();
    }

    @Override
    public void addUser(long teamId, long userId, String teamUUID) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        if (!team.getToken().toString().equals(teamUUID)){
            throw new NotValidDataException("Invalid team UUID");
        }
        if (team.getCurPlayers() < 5) {
            Personal personal = personalRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            List<Personal> users = team.getUsers();
            if (users == null) {
                users = new ArrayList<>();
            }
            if (!users.contains(personal)){
                users.add(personal);
                team.setCurPlayers(team.getCurPlayers() +1);
                teamRepository.save(team);
            }
        }
        else throw new FullTeamException("Team is full");
    }

    @Override
    public void deleteTeam(long teamId, long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        if (team.getAdmin().getId() == userId) {
            team.getUsers().stream()
                    .peek(user -> user.getTeams().remove(team))
                    .map(personalRepository::save)
                    .toList();
            team.getTournaments().stream()
                    .peek(tournament -> tournament.getTeams().remove(team))
                    .map(tournamentRepository::save)
                    .toList();
            if (team.getLogo() != null){
                long imageId = team.getLogo().getId();
                team.setLogo(null);
                imageRepository.deleteById(imageId);
            }
            team.setTournaments(null);
            team.setUsers(null);
            teamRepository.delete(team);
        }
    }
}
