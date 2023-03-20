package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(int filmId, int userId) {
        checkFilmId(filmId);
        checkUserId(userId);

        Film film = filmStorage.getById(filmId).get();

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        checkFilmId(filmId);
        checkUserId(userId);

        Film film = filmStorage.getById(filmId).get();

        if (film.getLikes() == null) {
            return;
        }

        film.getLikes().remove(userId);
    }

    public Collection<Film> getTop(int count) {
        return filmStorage.get().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * (f0.getLikes().size() - f1.getLikes().size()); //обратный порядок
    }

    private void checkUserId(int id) {
        userStorage.getById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + id + " не найден."));
    }

    private void checkFilmId(int id) {
        filmStorage.getById(id).orElseThrow(() -> new FilmNotFoundException("Фильм с ID = " + id + " не найден."));
    }
}
