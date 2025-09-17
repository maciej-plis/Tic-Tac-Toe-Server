package matthias.tictactoe.user;

import matthias.tictactoe.user.database.UserEntity;
import matthias.tictactoe.user.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static matthias.tictactoe.user.database.UserEntity.Type.GUEST;
import static matthias.tictactoe.user.database.UserEntity.Type.USER;

@Component
class UserMapper {

    public User toDomain(UserEntity userEntity) {
        return new User(
            userEntity.getUsername(),
            userEntity.getEmail(),
            userEntity.getPassword(),
            toDomains(userEntity.getRoles())
        );
    }

    public Set<User.Role> toDomains(Set<UserEntity.Role> roles) {
        return roles.stream().map(this::toDomain).collect(toSet());
    }

    public User.Role toDomain(UserEntity.Role role) {
        return User.Role.valueOf(role.name());
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
            .username(user.username())
            .email(user.email())
            .roles(toDtos(user.roles()))
            .build();
    }

    public Set<UserDto.Role> toDtos(Set<User.Role> roles) {
        return roles.stream().map(this::toDto).collect(toSet());
    }

    public UserDto.Role toDto(User.Role role) {
        return UserDto.Role.valueOf(role.name());
    }

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
            .type(USER)
            .username(user.username())
            .email(user.email())
            .password(user.password())
            .roles(toEntity(user.roles()))
            .build();
    }

    public Set<UserEntity.Role> toEntity(Set<User.Role> roles) {
        return roles.stream().map(this::toEntity).collect(toSet());
    }

    public UserEntity.Role toEntity(User.Role role) {
        return UserEntity.Role.valueOf(role.name());
    }

    public UserEntity toEntity(GuestUser guestUser) {
        return UserEntity.builder()
            .type(GUEST)
            .username(guestUser.name())
            .build();
    }
}
