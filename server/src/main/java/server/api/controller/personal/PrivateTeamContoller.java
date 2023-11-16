package server.api.controller.personal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.services.app.TeamService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/personal/teams")
public class PrivateTeamContoller {

    private final TeamService teamService;

    @PostMapping()
    public ResponseEntity<Long> createTeam(@RequestParam("name") String name, @RequestParam("ownerId") long ownerId, @RequestParam("image") MultipartFile image) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.createTeam(name,ownerId,image));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/logo")
    public ResponseEntity<String> updateTeamLogo(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {
                teamService.updateLogo(id,file);
                return ResponseEntity.ok("");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File loading error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Empty file");
        }
    }

}

