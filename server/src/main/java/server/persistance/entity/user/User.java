package server.persistance.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.tournament.Tournament;
import server.persistance.type.security.RoleType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @OneToOne
    private Image avatar;

    @ManyToMany(mappedBy = "users")
    private List<Team> teams;

    @OneToMany(mappedBy = "admin")
    private List<Tournament> tournaments;

    @Column(name = "tournament_admin")
    private boolean tournamentAdmin;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "enabled")
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleType.getAuthorities();
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
