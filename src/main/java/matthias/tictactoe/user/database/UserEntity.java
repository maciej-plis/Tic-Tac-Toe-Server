package matthias.tictactoe.user.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Type type;

    private String username;

    private String email;

    private String password;

    @Enumerated(STRING)
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"))
    private Set<Role> roles;

    public enum Type {
        GUEST,
        USER
    }

    public enum Role {
        USER,
        ADMIN
    }
}
