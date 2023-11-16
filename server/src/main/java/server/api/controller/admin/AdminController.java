package server.api.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.request.TournamentData;
import server.api.data.response.ResponseData;
import server.api.data.response.TournamentCreationData;
import server.services.app.TournamentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/private/admin")
public class AdminController {

    private final TournamentService tournamentService;

    @PostMapping("/tournament")
    public ResponseEntity<ResponseData<TournamentCreationData>> createTournament(@RequestBody TournamentData data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseData<>(tournamentService.createTournament(data)));
    }

    @PostMapping("/tournament/bracket/{bracketId}/stage/{stageId}/match/{matchId}/team/{teamId}")
    public ResponseEntity teamWon(@PathVariable long bracketId,@PathVariable long stageId, @PathVariable long matchId, @PathVariable long teamId){
        tournamentService.teamWon(bracketId,stageId,matchId,teamId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/tournament/{id}/image")
    @CrossOrigin()
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {
                tournamentService.updateTournamentImage(id,file);
                return ResponseEntity.ok("");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File loading error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Empty file");
        }
    }

    @DeleteMapping("tournament/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable long id){
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("tournament/cancel/{id}")
    public ResponseEntity<Void> cancelTournament(@PathVariable long id){
        tournamentService.cancelTournament(id);
        return ResponseEntity.noContent().build();
    }

}
