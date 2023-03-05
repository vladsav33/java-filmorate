package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilm;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int idCounter = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("The list of films returned");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (!film.validate()) {
            log.warn("Validation test failed");
            throw new ValidationException();
        }
        film.setId(++idCounter);
        films.put(film.getId(), film);
        log.info("Another film is added {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException, NoSuchFilm {
        if (!film.validate()) {
            log.warn("Validation test failed");
            throw new ValidationException();
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Such film was not found");
            throw new NoSuchFilm();
        }
        films.put(film.getId(), film);
        log.info("Film {} is updated", film);
        return film;
    }
}
