package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FilmControllerTest {

    @MockBean
    private FilmService filmService;
    @MockBean
    private ValidateService validateService;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void getFilms() {
        Film filmToCreate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(filmService.findAll()).thenReturn(List.of(filmToCreate));

        String response = mockMvc.perform(get("/films").contentType("application/json")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(filmService).findAll();
        assertEquals(objectMapper.writeValueAsString(List.of(filmToCreate)), response);
    }

    @SneakyThrows
    @Test
    void createValidFilm() {
        Film filmToCreate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(post("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToCreate))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        verify(filmService).createFilm(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @SneakyThrows
    @Test
    void createInvalidFilm() {
        Film filmToCreate = Film.builder().build();
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToCreate))).andExpect(status().isBadRequest());

        verify(filmService, never()).createFilm(any());
    }

    @SneakyThrows
    @Test
    void updateValidFilm() {
        Film filmToUpdate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(filmService.updateFilm(filmToUpdate)).thenReturn(filmToUpdate);

        String response = mockMvc.perform(put("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToUpdate))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(filmService).updateFilm(filmToUpdate);
        assertEquals(objectMapper.writeValueAsString(filmToUpdate), response);
    }

    @SneakyThrows
    @Test
    void updateInvalidFilm() {
        Film filmToUpdate = Film.builder().build();
        when(filmService.updateFilm(filmToUpdate)).thenReturn(filmToUpdate);

        mockMvc.perform(put("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToUpdate))).andExpect(status().isBadRequest());

        verify(filmService, never()).updateFilm(any());
    }
}
