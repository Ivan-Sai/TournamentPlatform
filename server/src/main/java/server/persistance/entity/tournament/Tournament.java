package server.persistance.entity.tournament;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.bracket.Bracket;
import server.persistance.entity.image.Image;
import server.persistance.entity.team.Team;
import server.persistance.entity.user.User;
import server.persistance.type.moduleTypes.TournamentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tournament extends BaseEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String discipline;

    @OneToOne
    Image logo;

    @ManyToMany(mappedBy = "tournaments",fetch = FetchType.EAGER)
    List<Team> teams;

    int currTeams;

    int maxTeams;

    int minTeamsToStart;

    LocalDateTime creationDate;

    LocalDateTime startDate;

    boolean isStarted;

    boolean isCanceled;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    User admin;

    @Enumerated(value = EnumType.STRING)
    TournamentType tournamentType;

    @OneToOne
    Bracket bracket;

    @ManyToOne
    Team winner;
}

