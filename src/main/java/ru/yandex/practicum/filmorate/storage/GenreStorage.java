package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAllGenres() {
        String sqlQuery = "SELECT genre_id, name FROM Genre order by genre_id";
        log.info("The list of genres returned");
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);

    }

    public Genre getGenreById(int genreId) {
        String sqlQuery = "SELECT genre_id, name FROM Genre WHERE genre_id=?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
            log.info("The genre was returned");
            return genre;
        } catch (IncorrectResultSizeDataAccessException exception) {
            log.warn("Such genre was not found");
            throw new NoSuchGenre("Such genre was not found");
        }
    }

    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT g.genre_id, g.name" +
                          " FROM filmgenre fr" +
                          " JOIN genre g ON fr.genre_id=g.genre_id" +
                          " WHERE film_id=?" +
                          " ORDER BY g.genre_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre, filmId);

    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Genre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
