package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(scripts = "classpath:schema.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;


    @Test
    public void testGetFilms() {
        Collection<Film> films = filmDbStorage.get();
        assertEquals(3, films.size());
    }

    @Test
    public void testGetFilmById() {
        Optional<Film> filmOptional = filmDbStorage.getById(1);

        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals(1, film.getId());
        assertEquals("1 film", film.getName());
        assertEquals("1 film desc", film.getDescription());
        assertEquals(LocalDate.of(2022, 01, 01), film.getReleaseDate());
        assertEquals(180, film.getDuration());
        assertEquals(1, film.getMpa().getId());
        assertEquals("G", film.getMpa().getName());
        assertEquals(1, film.getGenres().size());
    }

    @Test
    public void testGetFilmByIdNotFound() {
        Optional<Film> film = filmDbStorage.getById(999);

        assertTrue(film.isEmpty());
    }

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .name("New Film")
                .releaseDate(LocalDate.of(2022, 01, 01))
                .mpa(new MPA(1, "G"))
                .genres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"), new Genre(2, "Драма"))))
                .build();
        Film createdFilm = filmDbStorage.create(film);
        assertNotNull(createdFilm);
        assertTrue(createdFilm.getId() > 0);
        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getMpa().getId(), createdFilm.getMpa().getId());
        assertEquals(film.getMpa().getName(), createdFilm.getMpa().getName());
        assertEquals(film.getGenres().size(), createdFilm.getGenres().size());
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmDbStorage.getById(1).orElse(null);
        assertNotNull(film);
        film.setName("Updated Film");

        Optional<Film> updatedFilmOptional = filmDbStorage.update(film);
        assertTrue(updatedFilmOptional.isPresent());
        assertEquals(film.getName(), updatedFilmOptional.get().getName());
    }

    @Test
    public void testUpdateFilmNotFound() {
        Film film = Film.builder()
                .name("New Film")
                .releaseDate(LocalDate.of(2022, 01, 01))
                .mpa(new MPA(1, "G"))
                .genres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"), new Genre(2, "Драма"))))
                .build();
        film.setId(999);

        Optional<Film> updatedFilm = filmDbStorage.update(film);

        assertTrue(updatedFilm.isEmpty());
    }


    @Test
    public void testAddLike() {
        Film film = filmDbStorage.getById(3).orElse(null);
        assertNotNull(film);

        User user = User.builder()
                .email("test1@test.test")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user.setId(1);

        filmDbStorage.addLike(film, user);
        Film filmWithLike = filmDbStorage.getById(3).orElse(null);
        assertNotNull(filmWithLike);

        assertEquals(2, filmWithLike.getLikes().size());
        assertTrue(filmWithLike.getLikes().contains(1));
    }

    @Test
    public void testRemoveLike() {
        Film film = filmDbStorage.getById(1).orElse(null);
        assertNotNull(film);

        User user = User.builder()
                .email("test1@test.test")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user.setId(1);

        filmDbStorage.removeLike(film, user);
        Film filmWithoutLike = filmDbStorage.getById(1).orElse(null);
        assertNotNull(filmWithoutLike);

        assertEquals(3, filmWithoutLike.getLikes().size());
        assertFalse(filmWithoutLike.getLikes().contains(1));
    }
}


