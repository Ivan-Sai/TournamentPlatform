package server.api.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.data.dto.PersonalDto;
import server.api.data.dto.TeamDto;
import server.api.data.response.ResponseData;
import server.facade.TeamFacade;
import server.services.app.TeamService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/open/team/")
public class TeamController {

    private final TeamService teamService;
    private final TeamFacade teamFacade;


    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(teamFacade.findById(id));
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<ResponseData<byte[]>> getLogo(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseData<>(teamService.getLogo(id)));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<PersonalDto>> getPersonalsByTeamId(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamService.getPersonalsByTeam(id));
    }

}
