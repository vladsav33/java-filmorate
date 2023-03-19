package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.Film;
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
    private InMemoryUserStorage inMemoryUserStorage;
    @MockBean
    private InMemoryFilmStorage inMemoryFilmStorage;
    @MockBean
    private ValidateService validateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void getFilms() {
        Film filmToCreate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(inMemoryFilmStorage.get()).thenReturn(List.of(filmToCreate));

        String response = mockMvc.perform(get("/films").contentType("application/json")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(inMemoryFilmStorage).get();
        assertEquals(objectMapper.writeValueAsString(List.of(filmToCreate)), response);
    }

    @SneakyThrows
    @Test
    void createValidFIlm() {
        Film filmToCreate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(inMemoryFilmStorage.create(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(post("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToCreate))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        verify(inMemoryFilmStorage).create(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @SneakyThrows
    @Test
    void createInValidFIlm() {
        Film filmToCreate = Film.builder().build();
        when(inMemoryFilmStorage.create(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToCreate))).andExpect(status().isBadRequest());

        verify(inMemoryFilmStorage, never()).create(any());
    }

    @SneakyThrows
    @Test
    void updateValidFIlm() {
        Film filmToUpdate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(inMemoryFilmStorage.update(filmToUpdate)).thenReturn(filmToUpdate);

        String response = mockMvc.perform(put("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToUpdate))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(inMemoryFilmStorage).update(filmToUpdate);
        assertEquals(objectMapper.writeValueAsString(filmToUpdate), response);
    }

    @SneakyThrows
    @Test
    void updateInValidFIlm() {
        Film filmToUpdate = Film.builder().build();
        when(inMemoryFilmStorage.update(filmToUpdate)).thenReturn(filmToUpdate);

        mockMvc.perform(put("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToUpdate))).andExpect(status().isBadRequest());

        verify(inMemoryFilmStorage, never()).update(any());
    }

    @SneakyThrows
    @Test
    void updateFIlmWithWrongId() {
        Film filmToUpdate = Film.builder().name("name").description("description").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        when(inMemoryFilmStorage.update(filmToUpdate)).thenReturn(null);

        String response = mockMvc.perform(put("/films").contentType("application/json").content(objectMapper.writeValueAsString(filmToUpdate))).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        verify(inMemoryFilmStorage).update(filmToUpdate);
        assertEquals("", response);
    }
}
