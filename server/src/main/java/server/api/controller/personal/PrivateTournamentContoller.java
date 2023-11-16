package server.api.controller.personal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.services.app.TeamService;
import server.services.app.TournamentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/personal/tournament")
public class PrivateTournamentContoller {

    private final TournamentService tournamentService;

    @PutMapping("{tournamentId}/team/{teamId}")
    public ResponseEntity addTeam(@PathVariable long tournamentId,@PathVariable long teamId) {
        tournamentService.addTeam(tournamentId,teamId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

