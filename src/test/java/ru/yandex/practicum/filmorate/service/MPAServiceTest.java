package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dao.MPAStorage;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MPAServiceTest {

    private final MPAStorage mpaStorageMock = mock(MPAStorage.class);
    private final MPAService mpaService = new MPAService(mpaStorageMock);

    @Test
    void testFindAll() {
        List<MPA> expectedMPAs = Arrays.asList(
                new MPA(1, "G"),
                new MPA(2, "PG")
        );
        when(mpaStorageMock.get()).thenReturn(expectedMPAs);

        Collection<MPA> actualMPAs = mpaService.findAll();

        assertEquals(expectedMPAs, actualMPAs);
    }

    @Test
    void testFindByIdExists() {
        int mpaId = 1;
        MPA expectedMPA = new MPA(mpaId, "G");

        when(mpaStorageMock.getById(mpaId)).thenReturn(Optional.of(expectedMPA));

        MPA actualMPA = mpaService.findById(mpaId);

        assertEquals(expectedMPA, actualMPA);
    }

    @Test
    void testFindByIdNotExists() {
        int mpaId = 1;

        when(mpaStorageMock.getById(mpaId)).thenReturn(Optional.empty());

        assertThrows(MPANotFoundException.class, () -> mpaService.findById(mpaId));
    }
}