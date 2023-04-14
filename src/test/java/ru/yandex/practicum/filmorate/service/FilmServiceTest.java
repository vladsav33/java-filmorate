package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dao.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FilmServiceTest {

    private final UserStorage inMemoryUserStorage = Mockito.mock(InMemoryUserStorage.class);
    private final FilmStorage inMemoryFilmStorage = Mockito.mock(InMemoryFilmStorage.class);

    private final FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);

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
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.empty());
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(inMemoryFilmStorage).getById(filmId);
        assertEquals(exception.getMessage(), "Фильм с ID = " + filmId + " не найден.");
    }

    @Test
    void addLikeWhenUserIsNull() {
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> filmService.addLike(filmId, userId));
        verify(inMemoryFilmStorage).getById(filmId);
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void addLike() {
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.addLike(filmId, userId);

        verify(inMemoryFilmStorage).getById(filmId);
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(1, film.getLikes().size());
        assertEquals(Set.of(userId), film.getLikes());
    }

    @Test
    void removeLikeWhenFilmIsNull() {
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.empty());
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(inMemoryFilmStorage).getById(filmId);
        assertEquals(exception.getMessage(), "Фильм с ID = " + filmId + " не найден.");
    }

    @Test
    void removeLikeWhenUserIsNull() {
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> filmService.removeLike(filmId, userId));
        verify(inMemoryFilmStorage).getById(filmId);
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void removeLikeWhenZeroLikes() {
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.removeLike(filmId, userId);

        verify(inMemoryFilmStorage).getById(filmId);
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void removeLike() {
        film.getLikes().add(userId);
        when(inMemoryFilmStorage.getById(filmId)).thenReturn(Optional.of(film));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));

        filmService.removeLike(filmId, userId);

        verify(inMemoryFilmStorage).getById(filmId);
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void getTop() {
        when(inMemoryFilmStorage.get()).thenReturn(List.of(film, popularFilm));

        Collection<Film> top = filmService.getTop(3);

        verify(inMemoryFilmStorage).get();
        assertEquals(2, top.size());
        assertEquals(List.of(popularFilm, film), top);
    }

    @Test
    void getTopWithLimit() {
        when(inMemoryFilmStorage.get()).thenReturn(List.of(film, popularFilm));

        Collection<Film> top = filmService.getTop(1);

        verify(inMemoryFilmStorage).get();
        assertEquals(1, top.size());
        assertEquals(List.of(popularFilm), top);
    }
}