package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FilmServiceTest {

    private final UserStorage userStorage = Mockito.mock(UserStorage.class);
    private final FilmStorage filmStorage = Mockito.mock(FilmStorage.class);

    private final FilmService filmService = new FilmService(filmStorage, userStorage);

    private Film film = Film.builder()
            .name("name")
            .description("description")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(100)
            .build();

    private Film popularFilm = Film.builder()
            .name("name")
            .description("description")
            .releaseDate(LocalDate.of(2010, 1, 1))
            .duration(180)
            .build();

    private User user = User.builder()
            .email("test@test.test")
            .login("login")
            .name("name")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    private final int filmId = 1;
    private final int userId = 10;

    @BeforeEach
    void setLikes() {
        film.setLikes(new HashSet<>());
        popularFilm.setLikes(Set.of(1, 2, 3));
    }

    @Test
    void addLikeWhenFilmIsNull() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.empty());
        when(userStorage.getById(userId)).thenReturn(Optional.of(user));

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(filmStorage).getById(filmId);
        assertEquals(exception.getMessage(), "Фильм с ID = " + filmId + " не найден.");
    }

    @Test
    void addLikeWhenUserIsNull() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(userStorage.getById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(filmStorage).getById(filmId);
        verify(userStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void addLike() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(userStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.addLike(filmId, userId);

        verify(filmStorage).getById(filmId);
        verify(userStorage).getById(userId);
        verify(filmStorage).addLike(film, user);
    }

    @Test
    void removeLikeWhenFilmIsNull() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.empty());
        when(userStorage.getById(userId)).thenReturn(Optional.of(user));

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(filmStorage).getById(filmId);
        assertEquals(exception.getMessage(), "Фильм с ID = " + filmId + " не найден.");
    }

    @Test
    void removeLikeWhenUserIsNull() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(userStorage.getById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(filmStorage).getById(filmId);
        verify(userStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void removeLikeWhenZeroLikes() {
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(userStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.removeLike(filmId, userId);

        verify(filmStorage).getById(filmId);
        verify(userStorage).getById(userId);
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void removeLike() {
        film.getLikes().add(userId);
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(userStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.removeLike(filmId, userId);

        verify(filmStorage).getById(filmId);
        verify(userStorage).getById(userId);
        verify(filmStorage).removeLike(film, user);
    }

    @Test
    void getTop() {
        when(filmStorage.get()).thenReturn(List.of(film, popularFilm));

        Collection<Film> top = filmService.getTop(3);

        verify(filmStorage).get();
        assertEquals(2, top.size());
        assertEquals(List.of(popularFilm, film), top);
    }

    @Test
    void getTopWithLimit() {
        when(filmStorage.get()).thenReturn(List.of(film, popularFilm));

        Collection<Film> top = filmService.getTop(1);

        verify(filmStorage).get();
        assertEquals(1, top.size());
        assertEquals(List.of(popularFilm), top);
    }

    @Test
    public void testFindAll() {
        when(filmStorage.get()).thenReturn(List.of(film, popularFilm));

        Collection<Film> result = filmService.findAll();

        assertEquals(2, result.size());
        assertEquals(List.of(film, popularFilm), result);
        verify(filmStorage, times(1)).get();
    }

    @Test
    public void testFindById() {
        int filmId = 1;
        when(filmStorage.getById(filmId)).thenReturn(Optional.of(film));

        Film result = filmService.findById(filmId);

        assertEquals(film, result);
        verify(filmStorage, times(1)).getById(filmId);
    }

    @Test
    public void testFindByIdNotFound() {
        int filmId = 3;
        when(filmStorage.getById(filmId)).thenReturn(Optional.empty());

        assertThrows(FilmNotFoundException.class, () -> filmService.findById(filmId));
    }

    @Test
    public void testCreateFilm() {
        when(filmStorage.create(film)).thenReturn(film);

        Film result = filmService.createFilm(film);

        assertEquals(film, result);
        verify(filmStorage, times(1)).create(film);
    }

    @Test
    public void testUpdateFilm() {
        when(filmStorage.update(film)).thenReturn(Optional.of(film));

        Film result = filmService.updateFilm(film);

        assertEquals(film, result);
        verify(filmStorage, times(1)).update(film);
    }

    @Test
    public void testUpdateFilmNotFound() {
        when(filmStorage.update(film)).thenReturn(Optional.empty());

        assertThrows(FilmNotFoundException.class, () -> filmService.updateFilm(film));
    }
}