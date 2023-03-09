package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void FilmAddRightDetails() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void FilmAddEmptyName() {
        Film film = new Film(0, null, "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmAddOldReleaseDate() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("1850-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmAddNegativeDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), -200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmAddZeroDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmAddLongDescription() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}


