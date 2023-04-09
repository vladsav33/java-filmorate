package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
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

public class FilmTest {
    private static Validator validator;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;
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
        genreStorage = new GenreStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, genreStorage);
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void addFilmRightDetails() {
        Film film = new Film(1,  "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"),
                200, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addFilmEmptyName() {
        Film film = new Film(0, null, "Blockbuster", LocalDate.parse("2023-01-01"),
                200, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmOldReleaseDate() {
        Film film = new Film(0, "Avatar", "Blockbuster",
                LocalDate.parse("1850-01-01"),
                200, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmNegativeDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"),
                -200, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmZeroDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"),
                0, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmLongDescription() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200, null, null, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}


