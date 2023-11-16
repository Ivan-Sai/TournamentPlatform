package server.persistance.entity.bracket;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.tournament.Tournament;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bracket extends BaseEntity {

    @OneToMany
    List<Stage> stages;
}
