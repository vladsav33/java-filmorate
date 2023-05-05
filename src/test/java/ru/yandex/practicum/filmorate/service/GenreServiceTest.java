package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class GenreServiceTest {

    private final GenreStorage genreStorage = Mockito.mock(GenreStorage.class);

    private final GenreService genreService = new GenreService(genreStorage);

    @Test
    public void testFindAll() {
        List<Genre> genres = Arrays.asList(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма")
        );
        when(genreStorage.get()).thenReturn(genres);

        Collection<Genre> result = genreService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(new Genre(1, "Комедия")));
        assertTrue(result.contains(new Genre(2, "Драма")));
    }

    @Test
    public void testFindByIdExists() {
        int genreId = 1;
        Genre comedy = new Genre(genreId, "Комедия");
        when(genreStorage.getById(genreId)).thenReturn(Optional.of(comedy));

        Genre result = genreService.findById(genreId);

        assertEquals(comedy, result);
    }

    @Test
    public void testFindByIdNotExist() {
        int genreId = 3;
        when(genreStorage.getById(genreId)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.findById(genreId));
    }
}