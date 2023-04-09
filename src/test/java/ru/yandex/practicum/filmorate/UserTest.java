package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private static Validator validator;
    private UserStorage userStorage;
    private JdbcTemplate jdbcTemplate;
    private DriverManagerDataSource dataSource;

    @BeforeAll
    public static void setUpFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUpStorage() {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:./db/filmorate");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        jdbcTemplate = new JdbcTemplate(dataSource);
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void addUserRightDetails() {
        User user = new User(1, "jd", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addUserNoEmail() {
        User user = new User(1, "jd", "John Doe", null,
                LocalDate.parse("2000-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserWrongEmail() {
        User user = new User(1, "jd", "John Doe", "john.doe.hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserNoLogin() {
        User user = new User(1, null, "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserLoginWithSpace() {
        User user = new User(1, "j d", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserWrongBirthday() {
        User user = new User(1, "jd", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2025-11-30"), null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserEmptyName() {
        User user = new User(1, "jd", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void addFriend() {
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        User friend = new User(1, "jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"), null);
        user = userStorage.create(user);
        friend = userStorage.create(friend);
        userStorage.addFriend(user.getId(), friend.getId());
        user = userStorage.getUserById(user.getId());
        List<Integer> friends = user.getFriends();
        assertTrue(user.getFriends().contains(friend.getId()));
    }

    @Test
    public void deleteFriend() {
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        User friend = new User(1, "jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"), null);
        userStorage.create(user);
        userStorage.create(friend);
        userStorage.addFriend(user.getId(), friend.getId());
        userStorage.deleteFriend(user.getId(), friend.getId());
        assertNull(user.getFriends());
    }

    @Test
    public void getCommonFriend() {
        User userA = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        User userB = new User(2, "jack", null, "jack.doe@hotmail.com",
                LocalDate.parse("2002-09-28"), null);
        User friend = new User(3, "jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"), null);
        userStorage.create(userA);
        userStorage.create(userB);
        userStorage.create(friend);
        userStorage.addFriend(userA.getId(), friend.getId());
        userStorage.addFriend(userB.getId(), friend.getId());
        assertTrue(userStorage.getCommonFriends(userA.getId(), userB.getId()).contains(friend));
    }
}
