package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> films = new HashMap<>();
    List<Film> findAll();
    Film getFilmById(int filmId);
    Film create(Film film);
    Film update(Film film);
    Film likeFilm(int filmId, int userId);
    Film dislikeFilm(int filmId, int userId);
}
