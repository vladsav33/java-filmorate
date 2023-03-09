package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateServiceTest {

    private static ValidateService validateService = new ValidateService();

    @Test
    void validateUserNameCompletion() {
        User user = User.builder()
                .id(1)
                .email("test@test.test")
                .login("login")
                .name("")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        validateService.validateUser(user);

        assertEquals("login", user.getName());
    }

    @Test
    void validateFilmWrongReleaseDate() {
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(100)
                .build();

        FilmValidationException exception = assertThrows(FilmValidationException.class, () -> validateService.validateFilm(film));
        assertEquals(exception.getMessage(), "Дата релиза должна быть больше 28 декабря 1895 года - день рождения кино");
    }
}