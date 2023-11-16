package server.persistance.entity.team;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.image.Image;
import server.persistance.entity.tournament.Tournament;
import server.persistance.entity.user.Personal;
import server.persistance.entity.user.User;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team extends BaseEntity {

    @Column(nullable = false)
    String name;

    @OneToOne
    Image logo;

    int curPlayers;

    @ManyToMany
    List<Tournament> tournaments;

    @ManyToMany
    List<Personal> users;

    LocalDateTime creationDate;

    @ManyToOne()
    @JoinColumn(name = "admin_id")
    User admin;

    UUID token;
}
