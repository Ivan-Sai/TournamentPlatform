package server.api.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginData {
    private String email;
    private String password;
}
