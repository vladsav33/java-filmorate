package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmTest {
    @Test
    public void FilmAddRightDetails() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        assertTrue(film.validate());
    }

    @Test
    public void FilmAddEmptyName() {
        Film film = new Film(0, null, "Blockbuster", LocalDate.parse("2023-01-01"), 200);
        assertFalse(film.validate());
    }

    @Test
    public void FilmAddOldReleaseDate() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("1850-01-01"), 200);
        assertFalse(film.validate());
    }

    @Test
    public void FilmAddNegativeDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), -200);
        assertFalse(film.validate());
    }

    @Test
    public void FilmAddZeroDuration() {
        Film film = new Film(0, "Avatar", "Blockbuster", LocalDate.parse("2023-01-01"), 0);
        assertFalse(film.validate());
    }

    @Test
    public void FilmAddLongDescription() {
        Film film = new Film(0, "Avatar", "Blockbuster" + "*".repeat(200),
                LocalDate.parse("2023-01-01"), 200);
        assertFalse(film.validate());
    }
}
