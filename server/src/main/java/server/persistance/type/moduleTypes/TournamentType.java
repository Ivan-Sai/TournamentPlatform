package server.persistance.type.moduleTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum TournamentType {
    TYPE_5VS5("5vs5"),
    TYPE_1VS1("1vs1");

    private final String name;
}
