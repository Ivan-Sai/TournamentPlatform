package server.api.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterData {

    private String username;
    private String email;
    private String password;
}