package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
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
    @Test
    void findAll() {
        Film film1 = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        Film film2 = new Film(2, "Avatar 2", "Blockbuster",
                LocalDate.parse("2013-02-03"), 200, new HashMap<>(), new LinkedList<>(), null);
        filmStorage.create(film1);
        filmStorage.create(film2);
        List<Film> films = filmStorage.findAll();
        assertThat(films).contains(film1, film2);
    }

    @Test
    void update() {
        Film filmUpdated = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        filmStorage.create(filmUpdated);
        filmUpdated.setName("Avatar 2");
        filmUpdated.setDescription("Old blockbuster");
        filmUpdated.setDuration(195);
        filmUpdated.setReleaseDate(LocalDate.parse("2014-02-03"));
        filmStorage.update(filmUpdated);

        Film film = filmStorage.getFilmById(filmUpdated.getId());
        assertThat(film).isEqualTo(filmUpdated);
    }

    @Test
    void getFilmById() {
        Film filmNew = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        filmNew = filmStorage.create(filmNew);

        Film film = filmStorage.getFilmById(filmNew.getId());
        assertThat(film).isEqualTo(filmNew);
    }

    @Test
    void likeFilm() {
        Film filmNew = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        filmNew = filmStorage.create(filmNew);
        user = userDbStorage.create(user);

        filmStorage.likeFilm(filmNew.getId(), user.getId());
        Film film = filmStorage.getFilmById(filmNew.getId());
        assertThat(film.getLikes()).hasSize(1);
    }

    @Test
    void dislikeFilm() {
        Film filmNew = new Film(1, "Avatar 1", "Blockbuster",
                LocalDate.parse("2023-01-01"), 200, new HashMap<>(), new LinkedList<>(), null);
        User user = new User(1, "john", null, "john.doe@hotmail.com",
                LocalDate.parse("2000-11-30"), null);
        filmNew = filmStorage.create(filmNew);
        user = userDbStorage.create(user);

        filmStorage.likeFilm(filmNew.getId(), user.getId());
        Film film = filmStorage.getFilmById(filmNew.getId());
        assertThat(film.getLikes()).hasSize(1);

        filmStorage.dislikeFilm(filmNew.getId(), user.getId());
        film = filmStorage.getFilmById(filmNew.getId());
        assertThat(film.getLikes()).hasSize(0);
    }
}
