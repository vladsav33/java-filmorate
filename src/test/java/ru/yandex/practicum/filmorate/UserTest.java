package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
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
    @BeforeAll
    public static void setUpFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void AddUserRightDetails() {
        User user = new User(0, "jd", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void AddUserNoEmail() {
        User user = new User(0, "jd", "John Doe", null, LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void AddUserWrongEmail() {
        User user = new User(0, "jd", "John Doe", "john.doe.hotmail.com", LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void AddUserNoLogin() {
        User user = new User(0, null, "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void AddUserLoginWithSpace() {
        User user = new User(0, "j d", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void AddUserWrongBirthday() {
        User user = new User(0, "jd", "John Doe", "john.doe@hotmail.com", LocalDate.parse("2025-11-30"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void AddUserEmptyName() {
        User user = new User(0, "jd", null, "john.doe@hotmail.com", LocalDate.parse("2000-11-30"));
        assertEquals(user.getName(), user.getLogin());
    }
}
