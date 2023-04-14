package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

@Deprecated
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage extends BaseModelStorage<Film> implements FilmStorage {
}
