package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(scripts = "classpath:schema.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPADbStorageTest {

    private final MPADbStorage mpaDbStorage;

    @Test
    public void testGetMPA() {
        Collection<MPA> mpa = mpaDbStorage.get();
        assertEquals(5, mpa.size());
    }

    @Test
    public void testGetMPAById() {
        Optional<MPA> mpaOptional = mpaDbStorage.getById(1);

        assertTrue(mpaOptional.isPresent());
        MPA mpa = mpaOptional.get();
        assertEquals(1, mpa.getId());
        assertEquals("G", mpa.getName());
    }

    @Test
    public void testGetMPAByIdNotFound() {
        Optional<MPA> mpa = mpaDbStorage.getById(999);

        assertTrue(mpa.isEmpty());
    }
}