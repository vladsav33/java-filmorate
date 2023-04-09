package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchMpa;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> findAllMpas() {
        String sqlQuery = "SELECT rating_id, name FROM Rating ORDER BY rating_id";
        log.info("The list of ratings returned");
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);

    }

    public Mpa getMpaById(int mpaId) {
        String sqlQuery = "SELECT rating_id, name FROM Rating WHERE rating_id=?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
            log.info("The mpa was returned");
            return mpa;
        } catch (IncorrectResultSizeDataAccessException exception) {
            log.warn("Such rating was not found");
            throw new NoSuchMpa("Such rating was not found");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
