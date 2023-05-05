package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewStorage;
import ru.yandex.practicum.filmorate.exception.ReviewInsertDataBaseException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component("reviewDbStorage")
@Slf4j
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review getById(long reviewId) {
        String sqlQuery = "SELECT r.review_id AS reviewId, " +
                "r.content AS content, " +
                "r.is_positive AS isPositive, " +
                "r.creator_user_id AS userId, " +
                "r.reviewed_film_id AS filmId, " +
                "COALESCE(SUM(rl.score), 0) AS useful " +
                "FROM review AS r LEFT JOIN review_like AS rl ON r.review_id = rl.review_id " +
                "WHERE r.review_id = ? " +
                "GROUP BY r.review_id, r.content, r.is_positive, r.creator_user_id, r.reviewed_film_id;";

        final List<Review> reviews = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs), reviewId);
        if (reviews.size() != 1) {
            log.error("Отзыв с ID {} не существует", reviewId);
            throw new ReviewNotFoundException(String.format("Отзыв с ID %d не существует", reviewId));
        }

        return reviews.get(0);
    }

    @Override
    public List<Review> getReviewsByFilmId(int filmId, int count) {
        String sqlQuery = "SELECT r.review_id AS reviewId, " +
                "r.content AS content, " +
                "r.is_positive AS isPositive, " +
                "r.creator_user_id AS userId, " +
                "r.reviewed_film_id AS filmId, " +
                "COALESCE(SUM(rl.score), 0) AS useful " +
                "FROM review AS r LEFT JOIN review_like AS rl ON r.review_id = rl.review_id " +
                "WHERE r.reviewed_film_id = ? " +
                "GROUP BY r.review_id, r.content, r.is_positive, r.creator_user_id, r.reviewed_film_id " +
                "ORDER BY useful DESC, reviewId " +
                "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs), filmId, count);
    }

    @Override
    public List<Review> getReviewsByAllFilms(int count) {
        String sqlQuery = "SELECT r.review_id AS reviewId, " +
                "r.content AS content, " +
                "r.is_positive AS isPositive, " +
                "r.creator_user_id AS userId, " +
                "r.reviewed_film_id AS filmId, " +
                "COALESCE(SUM(rl.score), 0) AS useful " +
                "FROM review AS r LEFT JOIN review_like AS rl ON r.review_id = rl.review_id " +
                "GROUP BY r.review_id, r.content, r.is_positive, r.creator_user_id, r.reviewed_film_id " +
                "ORDER BY 6 DESC, 1 " +
                "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs), count);
    }

    @Override
    public Review createReview(Review review) {
        String reviewSqlQuery = "INSERT INTO review (creator_user_id, reviewed_film_id, content, is_positive) " +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(reviewSqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getFilmId());
            stmt.setString(3, review.getContent());
            stmt.setBoolean(4, review.getIsPositive());
            return stmt;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            log.error("Ошибка при добавлении отзыва в БД: {}", review);
            throw new ReviewInsertDataBaseException("Ошибка при добавлении отзыва в БД: " + review);
        }
        long createdId = keyHolder.getKey().longValue();

        return getById(createdId);
    }

    @Override
    public Review updateReview(Review review) {
        String reviewSqlQuery = "UPDATE review SET content = ?, is_positive = ? WHERE review_id = ?;";

        jdbcTemplate.update(reviewSqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());

        return getById(review.getReviewId());
    }

    @Override
    public void likeReview(long reviewId, int userId, int i) {
        String reviewLikeSqlQuery = "INSERT INTO review_like (review_id, user_id, score) VALUES (?, ?, ?);";

        jdbcTemplate.update(reviewLikeSqlQuery, reviewId, userId, i);
    }

    @Override
    public void removeReview(long reviewId) {
        String reviewSqlQuery = "DELETE FROM review WHERE review_id = ?;";

        jdbcTemplate.update(reviewSqlQuery, reviewId);
    }

    @Override
    public void removeLike(long reviewId, int userId, int i) {
        String removeLikeSqlQuery = "DELETE FROM review_like WHERE review_id =? AND user_id = ? AND score = ?;";

        jdbcTemplate.update(removeLikeSqlQuery, reviewId, userId, i);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        long reviewId = rs.getLong("reviewId");
        String content = rs.getString("content");
        boolean isPositive = rs.getBoolean("isPositive");
        int userId = rs.getInt("userId");
        int filmId = rs.getInt("filmId");
        int useful = rs.getInt("useful");

        return new Review(reviewId, content, isPositive, userId, filmId, useful);
    }
}
