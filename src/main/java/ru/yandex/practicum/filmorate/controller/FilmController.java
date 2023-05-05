package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.enums.ActionType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.SearchCategoryType;
import ru.yandex.practicum.filmorate.enums.SortCategoryType;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final ValidateService validateService;
    private final EventService eventService;

    @GetMapping("/search")
    public List<Film> search(@RequestParam(name = "query", defaultValue = "") String query,
                             @RequestParam(name = "by", defaultValue = "") List<SearchCategoryType> by) {
        log.info(String.format("Вывести фильмы, содержащие подстроку \"%s\" в полях: %s", query, by));
        return filmService.searchFilms(query, by.contains(SearchCategoryType.DIRECTOR), by.contains(SearchCategoryType.TITLE));
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Вывести все фильмы");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable int filmId) {
        log.info("Вывести фильм ID = {}", filmId);
        return filmService.findById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count,
                                 @RequestParam(defaultValue = "0") int genreId,
                                 @RequestParam(defaultValue = "0") int year) {
        log.info("Вывести ТОП {} фильмов, жанр: {}, год: {}", count, genreId, year);

        return filmService.getTop(count, genreId, year);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Добавляем лайк фильму ID = {} от пользователя ID = {}", filmId, userId);
        filmService.addLike(filmId, userId);
        eventService.createEvent(userId, ActionType.ADD, EventType.LIKE, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Удаляем лайк у фильма ID = {} от пользователя ID = {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        eventService.createEvent(userId, ActionType.REMOVE, EventType.LIKE, filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Valid Film film, BindingResult bindingResult) {
        log.info("Создаем фильм: {}", film);
        generateCustomValidateException(film, bindingResult);
        validateService.validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film, BindingResult bindingResult) {
        log.info("Обновляем фильм: {}", film);
        generateCustomValidateException(film, bindingResult);
        validateService.validateFilm(film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public void removeFilm(@PathVariable int filmId) {
        log.info("Удаляем фильм: {}", filmId);
        filmService.removeFilm(filmId);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId,
                                     @RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findFilmsByDirector(@PathVariable int directorId, @RequestParam SortCategoryType sortBy) {
        log.info("Вывести все фильмы режиссера {} с сортировкой по {}.", directorId, sortBy);
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    private void generateCustomValidateException(Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Ошибка в заполнении поля {} - {}. Фильм - {}", bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage(), film);
            throw new FilmValidationException("Ошибка в заполнении поля " + bindingResult.getFieldError().getField());
        }
    }
}
