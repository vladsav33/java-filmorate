package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FilmRepository {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter;

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film createFilm(Film film) {
        film.setId(++idCounter);
        films.put(film.getId(), film);
        log.debug("Создан фильм: {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}", film);
        return film;
    }
}
