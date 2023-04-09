package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("userStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sqlQuery = "SELECT user_id, login, name, email, birthday FROM users";
        log.warn("The list of users returned");
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        log.info("Another user {} is added", user);
        int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        return user;
    }

    public User update(User user) {
        String sqlQuery = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE user_id = ?";
        int result = jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        if (result == 0) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        log.info("User {} is updated", user);
        return user;
    }

    public User getUserById(int userId) {
        String sqlQuery = "SELECT user_id, login, name, email, birthday FROM users WHERE user_id=?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
            log.info("User {} is retrieved", userId);
        } catch (IncorrectResultSizeDataAccessException exception) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }

        sqlQuery = "SELECT friend_id FROM friendship WHERE user_id=?";
        List<Integer> friends = jdbcTemplate.query(sqlQuery, this::mapRowToFriend, userId);
        log.info("Friends {} were retrieved for the user {}", friends, userId);
        user.setFriends(friends);

        return user;
    }

    public User addFriend(int userId, int friendId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friendship");
        Friendship friendship = new Friendship(userId, friendId, false);
        log.info("Another friend {} is added for a user {}", friendId, userId);
        simpleJdbcInsert.execute(friendship.toMap());
        return null;
    }

    public User deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM Friendship WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Friend {} was deleted for a user {}", friendId, userId);

        return null;
    }

    public List<User> getFriends(int userId) {
        String sqlQuery = "SELECT u.user_id, u.login, u.name, u.email, u.birthday FROM users AS u "
        + "JOIN friendship AS f ON u.user_id=f.friend_id WHERE f.user_id=?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        String sqlQuery = "SELECT u.user_id," +
                          "u.name," +
                          "u.login," +
                          "u.email," +
                          "u.birthday " +
                          "FROM users as u " +
                          "JOIN friendship f1 ON u.user_id=f1.friend_id " +
                          "JOIN friendship f2 ON f1.friend_id = f2.friend_id " +
                          "WHERE f1.user_id=? AND f2.user_id=?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet == null) {
            return null;
        }
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getObject("birthday", LocalDate.class))
                .build();
    }

    private int mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }
}
