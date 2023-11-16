package server.api.controller.personal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.api.data.dto.PersonalDto;
import server.api.data.dto.TeamDto;
import server.api.data.response.ResponseData;
import server.facade.TeamFacade;
import server.services.app.PersonalService;
import server.services.app.TeamService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/personal")
public class PersonalController {

    private final PersonalService personalService;
    private final TeamFacade teamFacade;
    private final TeamService teamService;

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

    @DeleteMapping("/{userId}/teams/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable long userId,@PathVariable long teamId) {
        teamService.deleteTeam(teamId,userId);
        return ResponseEntity
                .status(HttpStatus.OK).body(null);
    }

    @PutMapping("/{userId}/teams/{teamId}")
    public ResponseEntity leaveTeam(@PathVariable long userId,@PathVariable long teamId) {
        personalService.leaveTeam(teamId,userId);
        return ResponseEntity
                .status(HttpStatus.OK).body(null);
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
                try {
                    personalService.updateAvatar(id,file);
                    return ResponseEntity.ok("");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File loading error: " + e.getMessage());
                }
            } else {
                return ResponseEntity.badRequest().body("Empty file");
        }
    }


    @PutMapping("/{userId}/inviteToTeam/{teamId}/{teamUUID}")
    public ResponseEntity<String> addUserToTeam (@PathVariable long userId, @PathVariable long teamId,@PathVariable String teamUUID) {
        teamService.addUser(teamId,userId,teamUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User added to team");
    }


}
