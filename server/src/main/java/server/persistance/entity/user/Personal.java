package server.persistance.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import server.persistance.type.security.RoleType;

@Entity
@DiscriminatorValue("PERSONAL")
public class Personal extends User{
    public Personal() {
        super();
        setRoleType(RoleType.PERSONAL);
    }
}
