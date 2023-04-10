package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaIntegrationTest {
    private final MpaStorage mpaStorage;

    @Test
    void findAllMpas() {
        List<Mpa> ratings = mpaStorage.findAllMpas();
        assertThat(ratings).hasSize(5);
    }

    @Test
    void getMpaById() {
        Mpa rating = mpaStorage.getMpaById(3);
        assertThat(rating).hasFieldOrPropertyWithValue("name", "PG-13");
    }
}