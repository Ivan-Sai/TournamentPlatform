package server.services.app.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.request.TournamentData;
import server.api.data.response.TournamentCreationData;
import java.util.Collections;
import server.persistance.entity.bracket.Bracket;
import server.persistance.entity.bracket.Match;
import server.persistance.entity.bracket.Stage;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.tournament.Tournament;
import server.persistance.entity.user.Admin;
import server.persistance.repository.app.*;
import server.persistance.repository.user.AdminRepository;
import server.persistance.type.moduleTypes.TournamentType;
import server.services.app.TournamentService;
import server.util.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final AdminRepository adminRepository;
    private final ImageRepository imageRepository;
    private final TeamRepository teamRepository;
    private final BracketRepository bracketRepository;
    private final MatchRepository matchRepository;
    private final StageRepository stageRepository;


    @Override
    public TournamentCreationData createTournament(TournamentData data) {
        Tournament tournament = new Tournament();
        tournament.setName(data.name());
        tournament.setDiscipline(data.discipline());
        tournament.setCurrTeams(0);
        tournament.setMaxTeams(data.maxTeams());
        tournament.setMinTeamsToStart(data.minTeams());
        tournament.setCreationDate(LocalDateTime.now());
        tournament.setStartDate(data.startDate());
        Admin admin = adminRepository.findById(data.adminId()).orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        admin.setTournamentAdmin(true);
        adminRepository.save(admin);
        tournament.setAdmin(admin);
        tournament.setTournamentType(TournamentType.valueOf(data.tournamentType()));
        Bracket bracket = new Bracket();
        bracketRepository.save(bracket);
        tournament.setBracket(bracket);
        tournamentRepository.save(tournament);
        return new TournamentCreationData(tournament.getId());
    }

    @Override
    public Tournament getTournament(long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
    }

    @Override
    public void updateTournamentImage(long id, MultipartFile image) throws IOException {
        Image image1 = new Image();
        image1.setName(image.getOriginalFilename());
        image1.setContentType(image.getContentType());
        image1.setBytes(ImageUtils.compressImage(image.getBytes()));
        imageRepository.save(image1);
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        Image toDel = tournament.getLogo();
        tournament.setLogo(image1);
        tournamentRepository.save(tournament);
        if (toDel!= null) imageRepository.delete(toDel);
    }

    @Override
    public byte[] getLogo(long id) {
        Image image = tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament not found")).getLogo();
        if (image == null) throw new EntityNotFoundException("Image not found");
        return ImageUtils.decompressImage(tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament not found")).getLogo().getBytes());
    }

    @Override
    public void deleteTournament(long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        Image image = tournament.getLogo();
        tournament.setLogo(null);
        if (image != null) imageRepository.delete(image);
        List<Team> teams = tournament.getTeams();
        if (teams == null) teams = new ArrayList<>();
        for (Team team : teams) {
            if (team.getTournaments().contains(tournament)){
                List<Tournament> team_tournaments = team.getTournaments();
                team_tournaments.remove(tournament);
                team.setTournaments(team_tournaments);
            }
        }
        tournamentRepository.delete(tournament);
    }

    @Override
    public void cancelTournament(long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        tournament.setCanceled(true);
        tournamentRepository.save(tournament);
    }

    @Override
    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    public List<Tournament> getTournamentsByFilter(String filter) {
        if (filter.equals("upcoming")) {
            List<Tournament> tournaments = tournamentRepository.findAll();
            return tournaments
                    .stream()
                    .filter(tournament -> tournament.getStartDate().isAfter(LocalDateTime.now()))
                    .toList();
        }
        else if (filter.equals("past")) {
            List<Tournament> tournaments = tournamentRepository.findAll();
            return tournaments
                    .stream()
                    .filter(tournament -> tournament.getStartDate().isBefore(LocalDateTime.now()))
                    .toList();
        }
        return new ArrayList<>();
    }

    @Override
    public void addTeam(long tournamentId, long teamId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        List<Team> teams = tournament.getTeams();
        if (teams != null) {
            tournament.getTeams().add(team);
        }
        else {
            teams = new ArrayList<>();
            teams.add(team);
        }
        tournament.setCurrTeams(tournament.getCurrTeams() + 1);
        tournamentRepository.save(tournament);
        List<Tournament> tournaments = team.getTournaments();
        if (tournaments != null) {
            tournaments.add(tournament);
        }
        else {
            tournaments = new ArrayList<>();
            tournaments.add(tournament);
        }
        teamRepository.save(team);
    }

    @Override
    public void teamWon(long bracketId,long stageId,long matchId,long teamId) {
        Bracket bracket = bracketRepository.findById(bracketId).orElseThrow(() -> new EntityNotFoundException("Bracket not found"));
        List<Stage> stages = bracket.getStages();
        Stage stage = stageRepository.findById(stageId).orElseThrow(() -> new EntityNotFoundException("Stage not found"));
        int stageIndex = stages.indexOf(stage);
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new EntityNotFoundException("Match not found"));
        if (stageIndex == stages.size()-1) {
            match.setWinnerId(teamId);
            matchRepository.save(match);
            Tournament tournament = tournamentRepository.findByBracketId(bracketId).orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
            tournament.setWinner(teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found")));
            tournamentRepository.save(tournament);
        }
        else if (stageIndex < stages.size()-1) {
            match.setWinnerId(teamId);
            matchRepository.save(match);
            List<Match> matches = stage.getMatches();
            int matchIndex = matches.indexOf(match) + 1;
            int matchIndexInNextStage;
            if (matchIndex % 2 == 0) {
                matchIndexInNextStage = matchIndex/2 -1;
            } else {
                matchIndexInNextStage = (matchIndex+1)/2 -1;
            }
            Stage nextStage = stages.get(stageIndex+1);
            List<Match> nextStageMatches = nextStage.getMatches();
            Match nextMatch = nextStageMatches.get(matchIndexInNextStage);
            List<Team> nextMatchTeams = nextMatch.getTeams();
            if (matchIndex % 2 == 0) {
                nextMatchTeams.add((teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Match not found"))));
                nextMatch.setTeam2Id(teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Match not found")).getId());
            } else {
                nextMatchTeams.add((teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Match not found"))));
                nextMatch.setTeam1Id(teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Match not found")).getId());            }
            matchRepository.save(nextMatch);
        }
        else {
            throw new EntityNotFoundException("Stage not found");
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void checkTournamentsStart() {
        System.out.println("checkTournamentsStart() started");
        List<Tournament> tournaments = tournamentRepository.findAllByIsStartedFalse();
        for (Tournament tournament : tournaments) {
            if (!tournament.isStarted() && tournament.getStartDate().isBefore(LocalDateTime.now())) {
                if (tournament.getTeams().size() < tournament.getMinTeamsToStart()){
                    cancelTournament(tournament.getId());
                }
                else {
                    startTournament(tournament);
                }
            }
        }

        System.out.println("checkTournamentsStart() ended");
    }

    private void startTournament(Tournament tournament ) {
        tournament.setStarted(true);
        Bracket bracket = tournament.getBracket();
        List<Stage> stages = new ArrayList<>();
        int numberOfMatches = getPowerOfTwo(tournament.getCurrTeams()).get(0);
        int numberOfStages = getPowerOfTwo(tournament.getCurrTeams()).get(1);
        //добавляем стадии в количестве полученным с функции
        for (int i = 0; i < numberOfStages; i++) {
            Stage stage = new Stage();
            stages.add(stage);
            stageRepository.save(stage);
        }
        //получаем и перемешиваем команды
        List<Team> teams = tournament.getTeams();
        Collections.shuffle(teams);

        //Cоздаем стартовые матчи из имеющихся команд
        List<Match> startMatches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i += 2) {
            Match match = new Match();
            matchRepository.save(match);


            List<Team> teamsInMatch = new ArrayList<>();
            teamsInMatch.add(teams.get(i));
            if (i + 1 < teams.size()) {
                teamsInMatch.add(teams.get(i + 1));
            }
            match.setTeams(teamsInMatch);
            match.setTeam1Id(teamsInMatch.get(0).getId());

            if (teamsInMatch.size() > 1) match.setTeam2Id(teamsInMatch.get(1).getId());


            matchRepository.save(match);

            startMatches.add(match);
        }
        //Пустые слоты заполняем пустыми матчами
        int emptySlots = numberOfMatches - startMatches.size();
        for (int i = 0; i < emptySlots; i++) {
            Match match = new Match();
            matchRepository.save(match);
            startMatches.add(match);
        }
        //закидываем и сохраняем первую стадию
        stages.get(0).setMatches(startMatches);
        stageRepository.save(stages.get(0));

        //генерация пустых матчей в последующих стадиях
        if (stages.size() > 1) {
            int i = 1;
            while (numberOfMatches > 1) {
                numberOfMatches /= 2;
                List<Match> matches = new ArrayList<>();
                for (int i1 = 0; i1 < numberOfMatches; i1++) {
                    Match match = new Match();
                    matchRepository.save(match);
                    matches.add(match);
                }
                stages.get(i).setMatches(matches);
                stageRepository.save(stages.get(i));
                i+=1;
            }
        }

        bracket.setStages(stages);
        bracketRepository.save(bracket);
        tournamentRepository.save(tournament);
    }


    private List<Integer> getPowerOfTwo(int number) {
        List<Integer> result = new ArrayList<>();
        int multNumber = 2;
        int multX2Number = 4;
        if (number > 2) {
            while (!(number > multNumber && number <= multX2Number)) {
                multNumber *= 2;
                multX2Number *= 2;
            }
            result.add(multX2Number/2);
            result.add((int) (Math.log(multX2Number) / Math.log(2)));
            return result;
        }
        result.add(number/2);
        result.add((int) (Math.log(number) / Math.log(2)));
        return  result;
    }
}
