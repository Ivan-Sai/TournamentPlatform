package server.api.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.data.dto.PersonalDto;
import server.api.data.dto.TeamDto;
import server.api.data.response.ResponseData;
import server.services.app.PersonalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/open/user")
public class UserController {

    private final PersonalService personalService;

    @GetMapping("/{id}/logo")
    public ResponseEntity<ResponseData<byte[]>> getLogo(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseData<>(personalService.getLogo(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDto> getPersonalById(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personalService.findPersonalById(id));
    }
    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TeamDto>> getTeamsByPersonal(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personalService.getTeamsByPersonalId(id));
    }
}
