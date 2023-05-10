package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> get();

    List<Film> search(String query, Boolean director, Boolean film);

    Optional<Film> getById(int id);

    Film create(Film film);

    Optional<Film> update(Film film);

    void addLike(Film film, User user, int rating);

    void removeLike(Film film, User user);

    List<Film> getFilmsByDirector(int directorId);

    List<Film> getPopularByGenreAndYear(int count, int genreId, int year, boolean byRating);

    void removeFilm(int filmId);

    List<Film> getFilmRecommendations(int userId);

    List<Film> getCommonFilms(int userId, int friendId);
}