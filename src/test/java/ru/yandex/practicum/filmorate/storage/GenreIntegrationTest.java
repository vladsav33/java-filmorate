package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreIntegrationTest {
    private final GenreStorage genreStorage;
    private final FilmDbStorage filmStorage;

    @Test
    void findAllGenres() {
        List<Genre> genres = genreStorage.findAllGenres();
        assertThat(genres).hasSize(6);
    }

    @Test
    void getGenreById() {
        Genre genre = genreStorage.getGenreById(2);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    void getGenresByFilmId() {
        Film film = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(),
                new LinkedList<>(), null);
        List<Map<String, Object>> genres = new LinkedList<>();
        Map<String, Object> genre = new HashMap<>();
        genre.put("id", 2);
        genres.add(genre);
        film.setGenres(genres);
        film = filmStorage.create(film);
        List<Genre> genreTest = genreStorage.getGenresByFilmId(film.getId());
        film = filmStorage.getFilmById(film.getId());
        assertThat(genreTest.get(0)).hasFieldOrPropertyWithValue("name", "Драма");
    }
}