package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Qualifier("genreDbStorage")
    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.get();
    }

    public Genre findById(int genreId) {
        return checkGenreId(genreId);
    }

    private Genre checkGenreId(int id) {
        return genreStorage.getById(id).orElseThrow(() -> new GenreNotFoundException("Жанр с ID = " + id + " не найден."));
    }
}
