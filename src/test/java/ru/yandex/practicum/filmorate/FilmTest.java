package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
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
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @BeforeAll
    public static void setUpFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUpStorage() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
    }

    @Test
    public void addFilmRightDetails() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addFilmEmptyName() {
        Film film = new Film(0, null, "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmOldReleaseDate() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("1850-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmNegativeDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), -200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmZeroDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmLongDescription() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void likeFilm() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200);
        User user = new User("john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        filmStorage.create(film);
        userStorage.create(user);
        filmStorage.likeFilm(film.getId(), user.getId());
        assertTrue(film.getLikes().contains(user.getId()));
    }

    @Test
    public void dislikeFilm() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200);
        User user = new User("john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"));
        filmStorage.create(film);
        userStorage.create(user);
        filmStorage.likeFilm(film.getId(), user.getId());
        filmStorage.dislikeFilm(film.getId(), user.getId());
        assertFalse(film.getLikes().contains(user.getId()));
    }
}


