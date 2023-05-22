package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSearchCriteria;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage extends BaseModelStorage<Film> implements FilmStorage {
    @Override
    @Deprecated
    public List<Film> search(String query, Boolean director, Boolean film) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }

    @Override
    public void addLike(Film film, User user, int rating) {
        film.getLikes().put(user.getId(), 0);
    }

    @Override
    public void removeLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    @Override
    @Deprecated
    public List<Film> getFilmsByDirector(int directorId) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }

    @Override
    @Deprecated
    public void removeFilm(int filmId) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }

    @Override
    @Deprecated
    public List<Film> getPopularByGenreAndYear(FilmSearchCriteria filmSearchCriteria) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }

    @Override
    @Deprecated
    public List<Film> getFilmRecommendations(int userId, boolean byRating) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }

    @Override
    @Deprecated
    public List<Film> getCommonFilms(int userId, int friendId) {
        throw new UnsupportedOperationException("Реализация метода существует только при работе с БД");
    }
}