package server.persistance.type.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    PERSONAL_READ("personal:read"),
    PERSONAL_CREATE("personal:create"),
    PERSONAL_UPDATE("personal:update"),
    PERSONAL_DELETE("personal:delete");

    private final String permission;
}
