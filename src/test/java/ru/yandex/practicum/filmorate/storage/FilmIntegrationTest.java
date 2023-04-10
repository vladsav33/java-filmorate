package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmIntegrationTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userDbStorage;
    private Film film1;
    private Film film2;

    @BeforeEach
    void setUpTest() {
        film1 = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        film2 = new Film(2, "Avatar 2", "Blockbuster",
                LocalDate.parse("2013-02-03"), 200, new HashMap<>(), new LinkedList<>(), null);
    }

    @Test
    void findAll() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        List<Film> films = filmStorage.findAll();
        assertThat(films).contains(film1, film2);
    }

    @Test
    void update() {
        filmStorage.create(film1);
        film1.setName("Avatar 2");
        film1.setDescription("Old blockbuster");
        film1.setDuration(195);
        film1.setReleaseDate(LocalDate.parse("2014-02-03"));
        filmStorage.update(film1);

        Film film = filmStorage.getFilmById(film1.getId());
        assertThat(film).isEqualTo(film1);
    }

    @Test
    void getFilmById() {
        film1 = filmStorage.create(film1);

        Film film = filmStorage.getFilmById(film1.getId());
        assertThat(film).isEqualTo(film1);
    }

    @Test
    void likeFilm() {
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        film1 = filmStorage.create(film1);
        user = userDbStorage.create(user);

        filmStorage.likeFilm(film1.getId(), user.getId());
        Film film = filmStorage.getFilmById(film1.getId());
        assertThat(film.getLikes()).hasSize(1);
    }

    @Test
    void dislikeFilm() {
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        film1 = filmStorage.create(film1);
        user = userDbStorage.create(user);

        filmStorage.likeFilm(film1.getId(), user.getId());
        Film film = filmStorage.getFilmById(film1.getId());
        assertThat(film.getLikes()).hasSize(1);

        filmStorage.dislikeFilm(film1.getId(), user.getId());
        film = filmStorage.getFilmById(film1.getId());
        assertThat(film.getLikes()).hasSize(0);
    }
}
