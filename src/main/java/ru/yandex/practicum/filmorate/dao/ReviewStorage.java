package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review getById(long reviewId);

    List<Review> getReviewsByFilmId(int filmId, int count);

    Review createReview(Review review);

    Review updateReview(Review review);

    void likeReview(long reviewId, int userId, int i);

    void removeReview(long reviewId);

    void removeLike(long reviewId, int userId, int i);

    List<Review> getReviewsByAllFilms(int count);
}
