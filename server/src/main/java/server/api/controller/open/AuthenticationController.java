package server.api.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.data.request.LoginData;
import server.api.data.request.RegisterData;
import server.api.data.response.AuthenticationData;
import server.api.data.response.ResponseData;
import server.services.security.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData<AuthenticationData>> register(@RequestBody RegisterData data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseData<>(authenticationService.register(data)));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<AuthenticationData>> login(@RequestBody LoginData data) {
        return ResponseEntity.ok(new ResponseData<>(authenticationService.login(data)));
    }
}
