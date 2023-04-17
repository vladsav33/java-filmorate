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
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Arrays;
import java.util.Collection;


@WebMvcTest(MPAController.class)
class MPAControllerTest {
    @MockBean
    private MPAService mpaService;

    @InjectMocks
    private MPAController mpaController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testFindAll() {
        Collection<MPA> mpa = Arrays.asList(
                new MPA(1, "G"),
                new MPA(2, "PG")
        );

        Mockito.when(mpaService.findAll()).thenReturn(mpa);

        mockMvc.perform(MockMvcRequestBuilders.get("/mpa"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("G"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("PG"));
    }

    @Test
    @SneakyThrows
    public void testFindById() {
        MPA mpa = new MPA(1, "G");

        Mockito.when(mpaService.findById(1)).thenReturn(mpa);

        mockMvc.perform(MockMvcRequestBuilders.get("/mpa/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("G"));
    }

    @Test
    @SneakyThrows
    public void testFindByIdThrowsMPANotFoundException() {
        int invalidId = 100;

        Mockito.when(mpaService.findById(invalidId))
                .thenThrow(new MPANotFoundException(String.format("Рейтинг с ID = %d не найден.", invalidId)));

        mockMvc.perform(MockMvcRequestBuilders.get("/mpa/" + invalidId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}