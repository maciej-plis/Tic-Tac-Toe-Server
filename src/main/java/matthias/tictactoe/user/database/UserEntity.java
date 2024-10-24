package matthias.tictactoe.user.database;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

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
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "id"))
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
