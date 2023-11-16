package server.persistance.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import server.persistance.type.security.RoleType;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User{

    public Admin() {
        super();
        setRoleType(RoleType.ADMIN);
    }
}
