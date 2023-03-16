package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getUserById(@RequestBody @PathVariable int id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilms(@RequestBody @RequestParam(required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        List<Film> result = filmStorage.findAll();
        result.sort((f1, f2) -> f2.getLikes().size() - f1.getLikes().size());
        return result.stream().limit(count).collect(Collectors.toList());
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeFilm(@RequestBody @PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film dislikeFilm(@RequestBody @PathVariable int id, @PathVariable int userId) {
        return filmService.dislikeFilm(id, userId);
    }
}
