package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    List<Film> findAll();
    Film getFilmById(int filmId);
    Film create(Film film);
    Film update(Film film);
    Film likeFilm(int filmId, int userId);
    Film dislikeFilm(int filmId, int userId);
}
