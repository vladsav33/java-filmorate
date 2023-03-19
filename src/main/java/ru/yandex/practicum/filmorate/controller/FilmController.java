package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.HttpMethodException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage inMemoryFilmStorage;
    private final ValidateService validateService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Вывести все фильмы");
        return inMemoryFilmStorage.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody @Valid Film film, BindingResult bindingResult) {
        log.info("Создаем фильм: {}", film);
        generateCustomValidateException(film, bindingResult);
        validateService.validateFilm(film);
        Film createdFilm = inMemoryFilmStorage.create(film);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film, BindingResult bindingResult) {
        log.info("Обновляем фильм: {}", film);
        generateCustomValidateException(film, bindingResult);
        validateService.validateFilm(film);
        Film updatedFilm = inMemoryFilmStorage.update(film);
        if (updatedFilm == null) {
            log.warn("Фильм с таким ID отсутствует: {}", film);
            throw new HttpMethodException("Фильм с таким ID отсутствует. Используйте метод POST");
        }
        return updatedFilm;
    }

    private void generateCustomValidateException(Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Ошибка в заполнении поля {} - {}. Фильм - {}", bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage(), film);
            throw new FilmValidationException("Ошибка в заполнении поля " + bindingResult.getFieldError().getField());
        }
    }
}
