package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Collection<Film> get();

    public Optional<Film> getById(int id);

    public Film create(Film film);

    public Optional<Film> update(Film film);

    public void addLike(Film film, User user);

    public void removeLike(Film film, User user);
}
