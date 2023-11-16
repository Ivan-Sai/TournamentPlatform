package server.facade.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.api.data.dto.BracketDto;
import server.api.data.dto.MatchDto;
import server.api.data.dto.StageDto;
import server.api.data.dto.TeamDto;
import server.api.data.response.TournamentResponseData;
import server.facade.TournamentFacade;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.bracket.Bracket;
import server.persistance.entity.bracket.Match;
import server.persistance.entity.bracket.Stage;
import server.persistance.entity.team.Team;
import server.persistance.entity.tournament.Tournament;
import server.persistance.repository.app.BracketRepository;
import server.persistance.repository.app.TeamRepository;
import server.services.app.TournamentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TournamentFacadeImpl implements TournamentFacade {

    private final TournamentService tournamentService;
    private final BracketRepository bracketRepository;
    private final TeamRepository teamRepository;



    @Override
    public TournamentResponseData getTournament(long id) {
        Tournament tournament = tournamentService.getTournament(id);
        Long winnerId = tournament.getWinner() != null ? tournament.getWinner().getId() : 0;

        return new TournamentResponseData(
                tournament.getName(),
                tournament.getDiscipline(),
                tournament.getMaxTeams(),
                tournament.getMinTeamsToStart(),
                tournament.getCurrTeams(),
                tournament.getCreationDate(),
                tournament.getStartDate(),
                tournament.getAdmin().getId(),
                tournament.getTournamentType().name(),
                tournament.isStarted(),
                tournament.isCanceled(),
                tournament.getId(),
                tournament.getBracket().getId(),
                winnerId
        );
    }

    @Override
    public List<TournamentResponseData> getAllSearchedTournaments(String filter, String discipline) {
        List<Tournament> tournaments = tournamentService.getTournaments();
        if (!filter.equals("undefined")){
            if (filter.equals("upcoming")){
                tournaments = tournaments
                        .stream()
                        .filter(tournament -> tournament.getStartDate().isAfter(LocalDateTime.now()))
                        .toList();
            }
            else if (filter.equals("past")){
                tournaments = tournaments
                        .stream()
                        .filter(tournament -> tournament.getStartDate().isBefore(LocalDateTime.now()))
                        .toList();
            }
        }
        if (!discipline.equals("undefined")) {
            if (discipline.equals("dota")){
                tournaments = tournaments
                        .stream()
                        .filter(tournament -> tournament.getDiscipline().equals("Dota 2"))
                        .toList();
            }
            if (discipline.equals("Valorant")){
                tournaments = tournaments
                        .stream()
                        .filter(tournament -> tournament.getDiscipline().equals("Valorant"))
                        .toList();
            }
        }
        List<TournamentResponseData> tournamentDto = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            long winnerId = tournament.getWinner() != null ? tournament.getWinner().getId() : 0;
            tournamentDto.add(new TournamentResponseData(
                    tournament.getName(),
                    tournament.getDiscipline(),
                    tournament.getMaxTeams(),
                    tournament.getMinTeamsToStart(),
                    tournament.getCurrTeams(),
                    tournament.getCreationDate(),
                    tournament.getStartDate(),
                    tournament.getAdmin().getId(),
                    tournament.getTournamentType().name(),
                    tournament.isStarted(),
                    tournament.isCanceled(),
                    tournament.getId(),
                    tournament.getBracket().getId(),
                    winnerId
            ));
        }
        return tournamentDto;
    }

    @Override
    public BracketDto getTournamentBracket(long id) {
        Bracket bracket = bracketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bracket not found"));
        List<Stage> stages = bracket.getStages();

        return new BracketDto(
                bracket.getId(),
                stages.stream()
                        .map(stage -> new StageDto(
                                stage.getId(),
                                stage.getMatches().stream()
                                        .map(match -> new MatchDto(
                                                match.getId(),
                                                createTeamDto(match.getTeam1Id() == null ? null : teamRepository.findById(match.getTeam1Id()).orElseThrow(()-> new EntityNotFoundException("Team not found"))),
                                                createTeamDto(match.getTeam2Id() == null ? null : teamRepository.findById(match.getTeam2Id()).orElseThrow(()-> new EntityNotFoundException("Team not found"))),
                                                match.getWinnerId()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    private TeamDto createTeamDto(Team team) {
        if (team == null) {
            return null; // Повертаємо пустий об'єкт TeamDto, можливо, з деякими значеннями за замовчуванням
        }

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
