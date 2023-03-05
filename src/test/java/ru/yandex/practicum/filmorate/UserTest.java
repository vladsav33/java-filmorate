package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void AddUserRightDetails() {
        User user = new User(0, "jd", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        assertTrue(user.validate());
    }

    @Test
    public void AddUserNoEmail() {
        User user = new User(0, "jd", "John Doe", null, LocalDate.parse("2000-11-30"));
        assertFalse(user.validate());
    }

    @Test
    public void AddUserWrongEmail() {
        User user = new User(0, "jd", "John Doe", "john.doe.hotmail.com", LocalDate.parse("2000-11-30"));
        assertFalse(user.validate());
    }

    @Test
    public void AddUserNoLogin() {
        User user = new User(0, null, "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        assertFalse(user.validate());
    }

    @Test
    public void AddUserLoginWithSpace() {
        User user = new User(0, "j d", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        assertFalse(user.validate());
    }

    @Test
    public void AddUserWrongBirthday() {
        User user = new User(0, "jd", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2025-11-30"));
        assertFalse(user.validate());
    }

    @Test
    public void AddUserEmptyName() {
        User user = new User(0, "jd", null, "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        assertEquals(user.getName(), user.getLogin());
    }
}
