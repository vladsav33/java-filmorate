package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.MPAStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;


    @Override
    public Collection<Film> get() {
        String sqlQuery =
                "SELECT *" +
                        "FROM films";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> getById(int id) {
        String sqlQuery =
                "SELECT * " +
                        "FROM films " +
                        "WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), id);

        // обрабатываем результат выполнения запроса
        if (films.isEmpty()) {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
        log.info("Найден фильм: {}", films.get(0));
        return Optional.of(films.get(0));
    }

    @Override
    public Film create(Film film) {
        String filmSqlQuery =
                "INSERT INTO films (film_nm, film_desc, release_dt, duration_min, rating_id) " +
                        "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updatedRowsCount = jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(filmSqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        if (updatedRowsCount == 0) {
            log.info("Произошла ошибка при добавлении фильма {} в базу данных", film);
            return null;
        }

        int filmId = (int) keyHolder.getKey().longValue();

        if (film.getGenres() == null) {
            Film createdFilm = getById(filmId).orElse(null);
            log.info("Фильм {} добавлен в базу данных", createdFilm);
            return createdFilm;
        }

        String genreSqlQuery =
                "INSERT INTO film_x_genre (film_id, genre_id) " +
                        "VALUES (?, ?)";

        film.getGenres().forEach(genre -> {
            jdbcTemplate.update(genreSqlQuery,
                    filmId,
                    genre.getId());
        });

        Film createdFilm = getById(filmId).orElse(null);
        log.info("Фильм {} добавлен в базу данных", createdFilm);
        return createdFilm;
    }

    @Override
    public Optional<Film> update(Film film) {
        String filmSqlQuery =
                "UPDATE films " +
                        "SET film_nm = ?, film_desc = ?, release_dt = ?, duration_min = ?, rating_id = ? " +
                        "WHERE film_id = ?";
        int updatedRowsCount = jdbcTemplate.update(filmSqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (updatedRowsCount == 0) {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            return Optional.empty();
        }

        String genreDeleteSqlQuery =
                "DELETE FROM film_x_genre " +
                        "WHERE film_id = ?";

        jdbcTemplate.update(genreDeleteSqlQuery, film.getId());

        if (film.getGenres() == null) {
            Optional<Film> updatedFilm = this.getById(film.getId());
            log.info("Фильм {} обновлен в базе данных", updatedFilm);
            return updatedFilm;
        }

        String genreSqlQuery =
                "INSERT INTO film_x_genre (film_id, genre_id) " +
                        "VALUES (?, ?)";

        film.getGenres().forEach(genre -> {
            jdbcTemplate.update(genreSqlQuery,
                    film.getId(),
                    genre.getId());
        });

        Optional<Film> updatedFilm = this.getById(film.getId());
        log.info("Фильм {} обновлен в базе данных", updatedFilm);
        return updatedFilm;
    }

    @Override
    public void addLike(Film film, User user) {
        String sqlQuery =
                "MERGE INTO likes (film_id, user_id) " +
                        "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void removeLike(Film film, User user) {
        String sqlQuery =
                "DELETE FROM likes " +
                        "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("film_id");
        Film film = Film.builder()
                .name(rs.getString("film_nm"))
                .description(rs.getString("film_desc"))
                .releaseDate(rs.getDate("release_dt").toLocalDate())
                .duration(rs.getInt("duration_min"))
                .mpa(mpaStorage.getById(rs.getInt("rating_id")).orElseGet(null))
                .genres(getGenresByFilmId(filmId))
                .likes(getLikesByFilmId(filmId))
                .build();
        film.setId(filmId);
        return film;
    }

    private Set<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery =
                "SELECT genre_id " +
                        "FROM film_x_genre " +
                        "WHERE film_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId)
                .stream()
                .map(genreStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Set<Integer> getLikesByFilmId(int filmId) {
        String sqlQuery =
                "SELECT user_id " +
                        "FROM likes " +
                        "WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
        return new HashSet<>(likes);
    }

}
