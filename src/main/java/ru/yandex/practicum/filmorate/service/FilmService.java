package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilm;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@Slf4j
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film likeFilm(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Such film was not found");
            throw new NoSuchFilm();
        }
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return filmStorage.likeFilm(filmId, userId);
    }
    public Film dislikeFilm(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Such film was not found");
            throw new NoSuchFilm();
        }
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return filmStorage.dislikeFilm(filmId, userId);
    }
}
