package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilm;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }


    public Film likeFilm(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Such film was not found");
            throw new NoSuchFilm("Such film was not found");
        }
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return filmStorage.likeFilm(filmId, userId);
    }
    public Film dislikeFilm(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Such film was not found");
            throw new NoSuchFilm("Such film was not found");
        }
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return filmStorage.dislikeFilm(filmId, userId);
    }

    public List<Film> getFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
