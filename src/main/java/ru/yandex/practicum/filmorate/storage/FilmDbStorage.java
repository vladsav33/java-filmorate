package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilm;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Likes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("filmStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    public List<Film> findAll() {
        String sqlQuery = "SELECT f.film_id," +
                " f.name," +
                " f.description," +
                " f.releaseDate," +
                " f.duration," +
                " f.rating_id," +
                " r.name as rating_name," +
                " g.genre_id," +
                " g.name as genre_name," +
                " COUNT(l.user_id) as user_likes" +
                " FROM films f LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                " LEFT JOIN filmgenre fr ON f.film_id=fr.film_id" +
                " LEFT JOIN genre g ON fr.genre_id=g.genre_id" +
                " LEFT JOIN likes l ON f.film_id=l.film_id" +
                " GROUP BY f.name, f.description, f.name, f.film_id, f.releaseDate, f.duration, f.rating_id, r.name, g.genre_id, g.name" +
                " ORDER BY user_likes DESC";
        log.info("The list of films returned");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        log.info("Another film is added {}", film);

        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);
        if (film.getGenres() == null) {
            return film;
        }
        for (Map<String, Object> genre : film.getGenres()) {
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("filmGenre");
            FilmGenre filmGenre = new FilmGenre(film.getId(), (Integer) genre.get("id"));
            simpleJdbcInsert.execute(filmGenre.toMap());
        }
        return film;
    }

    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id=? " +
                          "WHERE film_id = ?";
        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().get("id"),
                film.getId());

        if (result == 0) {
            log.warn("Such film was not found");
            throw new NoSuchFilm("Such film was not found");
        }

        log.info("Film {} is updated", film);
        sqlQuery = "DELETE FROM filmgenre WHERE film_id=?";
        log.info("Genres were deleted for the film {}", film.getId());
        jdbcTemplate.update(sqlQuery, film.getId());

        if (film.getGenres() == null) {
            return film;
        }
        Set<Map<String, Object>> genres = new HashSet<>();
        genres.addAll(film.getGenres());
        for (Map<String, Object> genre : genres) {
            log.info("Film {} has the genre {}", film.getId(), genre.get("id"));
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("FilmGenre");
            FilmGenre filmGenre = new FilmGenre(film.getId(), (Integer) genre.get("id"));
            simpleJdbcInsert.execute(filmGenre.toMap());
        }
        return getFilmById(film.getId());
    }

    public Film getFilmById(int filmId) {
        Film film;
        String sqlQuery = "SELECT f.film_id," +
                                " f.name," +
                                " f.description," +
                                " f.releaseDate," +
                                " f.duration," +
                                " f.rating_id," +
                                " r.name as rating_name" +
                                " FROM films f LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                                " WHERE f.film_id=?";
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmById, filmId);
            log.info("The film was returned");
        } catch (IncorrectResultSizeDataAccessException exception) {
            log.warn("Such film was not found");
            throw new NoSuchFilm("Such film was not found");
        }
        List<Genre> genres = genreStorage.getGenresByFilmId(filmId);
        List<Map<String, Object>> genresToFilm = new LinkedList<>();
        for (Genre genre : genres) {
            Map<String, Object> genreToFilm = new HashMap<>();
            genreToFilm.put("id", genre.getId());
            genreToFilm.put("name", genre.getName());
            genresToFilm.add(genreToFilm);
        }
        film.setGenres(genresToFilm);

        sqlQuery = "SELECT user_id FROM likes WHERE film_id=?";
        List<Integer> likes = jdbcTemplate.query(sqlQuery, this::mapRowToLike, filmId);
        film.setLikes(likes);
        return film;
    }

    public Film likeFilm(int filmId, int userId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("likes");
        log.info("Another like from the user {} is added for the film {}", userId, filmId);
        Likes likes = new Likes(userId, filmId);
        simpleJdbcInsert.execute(likes.toMap());
        return null;
    }

    public Film dislikeFilm(int filmId, int userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Like was deleted for the film {}", filmId);
        return null;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Object> mpa = new HashMap<>();
        if (resultSet.getString("rating_name") != null) {
            mpa.put("id", resultSet.getInt("rating_id"));
            mpa.put("name", resultSet.getString("rating_name"));
        }
        List<Map<String, Object>> genres = new LinkedList<>();
        Map<String, Object> genre = new HashMap<>();
        genre.put("id", resultSet.getInt("genre_id"));
        genre.put("name", resultSet.getString("genre_name"));
        if (resultSet.getString("genre_name") != null) {
            genres.add(genre);
        }
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private Film mapRowToFilmById(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Object> mpa = new HashMap<>();
        if (resultSet.getString("rating_name") != null) {
            mpa.put("id", resultSet.getInt("rating_id"));
            mpa.put("name", resultSet.getString("rating_name"));
        }
        List<Map<String, Object>> genres = new LinkedList<>();
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .mpa(mpa)
                .build();
    }

    private int mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }
}
