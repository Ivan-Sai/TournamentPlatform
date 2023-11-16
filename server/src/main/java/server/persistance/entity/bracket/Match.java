package server.persistance.entity.bracket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.team.Team;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "match_table")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Match extends BaseEntity {

    @ManyToMany
    @JoinTable(
            name = "match_team",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams;

    @Column(name = "team1_id")
    private Long team1Id;

    @Column(name = "team2_id")
    private Long team2Id;

    private long winnerId;
}
