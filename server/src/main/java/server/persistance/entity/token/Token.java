package server.persistance.entity.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import server.persistance.entity.BaseEntity;
import server.persistance.entity.user.User;
import server.persistance.type.security.TokenType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Token extends BaseEntity {

    @Column(nullable = false, unique = true,length = 355)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    private Boolean expired;
    private Boolean revoked;

    @OneToOne
    private User user;

    public Token() {
        this.tokenType = TokenType.BEARER;
    }
}
