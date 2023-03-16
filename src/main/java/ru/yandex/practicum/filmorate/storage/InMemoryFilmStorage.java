package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilm;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int idCounter = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        log.info("The list of films returned");
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        film.setId(++idCounter);
        films.put(film.getId(), film);
        log.info("Another film is added {}", film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Such film was not found");
            throw new NoSuchFilm();
        }
        films.put(film.getId(), film);
        log.info("Film {} is updated", film);
        return film;
    }

    public Film getFilmById(int filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("Such film was not found");
            throw new NoSuchFilm();
        }
        return films.get(filmId);
    }

    public Film likeFilm(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
        log.info("New like was added for the film {}", filmId);
        return film;
    }

    public Film dislikeFilm(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
        log.info("Like was deleted for the film {}", filmId);
        return film;
    }
}
