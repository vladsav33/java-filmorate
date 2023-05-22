package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.enums.SortCategoryType;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.SortByValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSearchCriteria;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Qualifier("filmDbStorage")
    @NonNull
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    @NonNull
    private final UserStorage userStorage;
    @NonNull
    private final DirectorService directorService;

    public List<Film> findAll() {
        return filmStorage.get();
    }

    public List<Film> searchFilms(String query, Boolean director, Boolean film) {
        return filmStorage.search(query, director, film)
                .stream()
                .sorted(this::compareByLikes)
                .collect(Collectors.toList());
    }

    public Film findById(int filmId) {
        return checkFilmId(filmId);
    }

    public Film createFilm(Film film) {
        film.setLikes(new HashMap<>());
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashMap<>());
        }
        return filmStorage.update(film).orElseThrow(()
                -> new FilmNotFoundException("Фильм с ID = " + film.getId() + " не найден."));
    }

    public void addLike(int filmId, int userId, int rating) {
        Film film = checkFilmId(filmId);
        User user = checkUserId(userId);

        filmStorage.addLike(film, user, rating);

        log.debug("Добавлен лайк от пользователя ID = {} в фильм: {} с рейтингом {}", userId, film, rating);
    }

    public void removeLike(int filmId, int userId) {
        Film film = checkFilmId(filmId);
        User user = checkUserId(userId);

        filmStorage.removeLike(film, user);

        log.debug("Удален лайк от пользователя ID = {} в фильме: {}", userId, film);
    }

    public List<Film> getTop(FilmSearchCriteria criteria) {
        log.info("Получаем список из {} популярных фильмов, жанр {}, год {}",
                criteria.getCount(), criteria.getGenreId(), criteria.getYear());
        return filmStorage.getPopularByGenreAndYear(criteria);
    }

    public List<Film> getFilmsByDirector(int directorId, SortCategoryType sortBy) {
        directorService.getIfDirectorExists(directorId);
        checkSortByParam(sortBy);
        List<Film> films = new ArrayList<>(filmStorage.getFilmsByDirector(directorId));

        Map<SortCategoryType, Comparator<Film>> sortingMethodByType = new HashMap<>();
        sortingMethodByType.put(SortCategoryType.LIKES, this::compareByLikes);
        sortingMethodByType.put(SortCategoryType.RATING, this::compareByRating);
        sortingMethodByType.put(SortCategoryType.YEAR, this::compareByYear);
        films.sort(sortingMethodByType.get(sortBy));

        return films;
    }

    public void removeFilm(int filmId) {
        Film film = checkFilmId(filmId);

        filmStorage.removeFilm(filmId);

        log.debug("Удален фильм: {}", film);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        return filmStorage.getCommonFilms(userId, friendId).stream()
                .sorted(this::compareByLikes)
                .collect(Collectors.toList());
    }

    private int compareByLikes(Film f0, Film f1) {
        return -1 * (f0.getLikes().size() - f1.getLikes().size()); //обратный порядок
    }

    private int compareByRating(Film f0, Film f1) {
        return Double.compare(f0.getAverageRating(), f1.getAverageRating());
    }

    private int compareByYear(Film f0, Film f1) {
        if (f0.getReleaseDate().isBefore(f1.getReleaseDate())) {
            return -1;
        } else if (f0.getReleaseDate().equals(f1.getReleaseDate())) {
            return 0;
        } else {
            return 1;
        }
    }

    private User checkUserId(int id) {
        return userStorage.getById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + id + " не найден."));
    }

    private Film checkFilmId(int id) {
        return filmStorage.getById(id).orElseThrow(() -> new FilmNotFoundException("Фильм с ID = " + id + " не найден."));
    }

    private void checkSortByParam(SortCategoryType sortType) {
        if (sortType != SortCategoryType.YEAR && sortType != SortCategoryType.LIKES) {
            throw new SortByValidationException("Некорректно введен параметр сортировки.");
        }
    }
}
