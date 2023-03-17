package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private static Validator validator;
    private UserStorage userStorage;

    @BeforeAll
    public static void setUpFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUpStorage() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    public void addUserRightDetails() {
        User user = new User("jd", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addUserNoEmail() {
        User user = new User("jd", "John Doe", null,
                LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserWrongEmail() {
        User user = new User("jd", "John Doe", "john.doe.hotmail.com",
                LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserNoLogin() {
        User user = new User(null, "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserLoginWithSpace() {
        User user = new User("j d", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserWrongBirthday() {
        User user = new User("jd", "John Doe", "john.doe@hotmail.com",
                LocalDate.parse("2025-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addUserEmptyName() {
        User user = new User("jd", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void addFriend() {
        User user = new User("john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        User friend = new User("jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"));
        userStorage.create(user);
        userStorage.create(friend);
        userStorage.addFriend(user.getId(), friend.getId());
        assertTrue(user.getFriends().contains(friend.getId()));
        assertTrue(friend.getFriends().contains(user.getId()));
    }

    @Test
    public void deleteFriend() {
        User user = new User("john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        User friend = new User("jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"));
        userStorage.create(user);
        userStorage.create(friend);
        userStorage.addFriend(user.getId(), friend.getId());
        userStorage.deleteFriend(user.getId(), friend.getId());
        assertFalse(user.getFriends().contains(friend.getId()));
        assertFalse(friend.getFriends().contains(user.getId()));
    }

    @Test
    public void getCommonFriend() {
        User userA = new User("john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        User userB = new User("jack", null, "jack.doe@hotmail.com",
                LocalDate.parse("2002-09-28"));
        User friend = new User("jane", null, "jane.doe@hotmail.com",
                LocalDate.parse("2001-10-29"));
        userStorage.create(userA);
        userStorage.create(userB);
        userStorage.create(friend);
        userStorage.addFriend(userA.getId(), friend.getId());
        userStorage.addFriend(userB.getId(), friend.getId());
        assertTrue(userStorage.getCommonFriends(userA.getId(), userB.getId()).contains(friend));
    }
}
