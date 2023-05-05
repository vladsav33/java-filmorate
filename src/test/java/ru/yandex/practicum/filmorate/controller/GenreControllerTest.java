package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testFindAll() {
        List<Genre> genres = Arrays.asList(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма")
        );

        Mockito.when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(MockMvcRequestBuilders.get("/genres"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Комедия"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Драма"));
    }

    @Test
    @SneakyThrows
    public void testFindById() {
        Genre genre = new Genre(1, "Комедия");

        Mockito.when(genreService.findById(1)).thenReturn(genre);

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Комедия"));
    }

    @Test
    @SneakyThrows
    public void testFindByIdThrowsGenreNotFoundException() {
        int invalidId = 100;

        Mockito.when(genreService.findById(invalidId))
                .thenThrow(new GenreNotFoundException(String.format("Жанр с ID = %d не найден.", invalidId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/" + invalidId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}