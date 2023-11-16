package server.services.security;

import server.api.data.request.LoginData;
import server.api.data.request.RegisterData;
import server.api.data.response.AuthenticationData;
import server.exception.UserExistsException;

public interface AuthenticationService {
    AuthenticationData register(RegisterData data);
    AuthenticationData login(LoginData data);
}
