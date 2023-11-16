package server.api.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.api.data.dto.BracketDto;
import server.api.data.response.ResponseData;
import server.api.data.response.TournamentResponseData;
import server.facade.TournamentFacade;
import server.services.app.TournamentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/open/tournament")
public class TournamentController {

    private final TournamentFacade tournamentFacade;
    private final TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<List<TournamentResponseData>> getAllTournament(@RequestParam String filter,@RequestParam String discipline) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tournamentFacade.getAllSearchedTournaments(filter,discipline));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<TournamentResponseData>> getTournament(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseData<>(tournamentFacade.getTournament(id)));
    }

    @GetMapping("/bracket/{id}")
    public ResponseEntity<BracketDto> getTournamentBracket(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tournamentFacade.getTournamentBracket(id));
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<ResponseData<byte[]>> getLogo(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseData<>(tournamentService.getLogo(id)));
    }


}
