package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.ActionType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.exception.ReviewValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EventService eventService;

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        log.info("Вывести отзыв ID = {}", id);
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> getFilmReviews(@RequestParam(defaultValue = "0") int filmId, @RequestParam(defaultValue = "10") int count) {
        log.info("Вывести список отзывов к фильму ID = {}, в количестве {}", filmId, count);
        return reviewService.getFilmReviews(filmId, count);
    }

    @PostMapping
    public Review createReview(@RequestBody @Valid Review review, BindingResult bindingResult) {
        log.info("Создать отзыв: {}", review);
        generateCustomValidateException(review, bindingResult);
        Review createdReview = reviewService.createReview(review);
        eventService.createEvent(createdReview.getUserId(), ActionType.ADD, EventType.REVIEW,
                createdReview.getReviewId());
        return createdReview;
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review, BindingResult bindingResult) {
        log.info("Обновить отзыв: {}", review);
        generateCustomValidateException(review, bindingResult);
        Review updatedReview = reviewService.updateReview(review);
        eventService.createEvent(updatedReview.getUserId(), ActionType.UPDATE, EventType.REVIEW,
                updatedReview.getReviewId());
        return updatedReview;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable long id, @PathVariable int userId) {
        log.info("User с ID = {} ставит лайк отзыву с ID = {}", userId, id);
        reviewService.likeReview(id, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable long id, @PathVariable int userId) {
        log.info("User с ID = {} ставит дизлайк отзыву с ID = {}", userId, id);
        reviewService.likeReview(id, userId, false);
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable long id) {
        log.info("Удалить отзыв с ID: {}", id);
        Review reviewToRemove = reviewService.findById(id);
        reviewService.removeReview(id);
        eventService.createEvent(reviewToRemove.getUserId(), ActionType.REMOVE, EventType.REVIEW,
                reviewToRemove.getReviewId());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable int userId) {
        log.info("User с ID = {} удаляет лайк к отзыву с ID = {}", userId, id);
        reviewService.removeLike(id, userId, true);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable long id, @PathVariable int userId) {
        log.info("User с ID = {} удаляет дизлайк к отзыву с ID = {}", userId, id);
        reviewService.removeLike(id, userId, false);
    }

    private void generateCustomValidateException(Review review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.getFieldError() != null) {
                String fieldName = bindingResult.getFieldError().getField();
                String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
                log.error("Ошибка в заполнении поля {} - {}. Отзыв - {}", fieldName, defaultMessage, review);

                if (defaultMessage != null && defaultMessage.equals("review notnull")) {
                    throw new ReviewValidationException("Поле '" + fieldName + "' не должно быть пустым");
                }

                throw new ReviewValidationException("Ошибка в заполнении поля: " + fieldName + ". " +
                        "Сообщение: " + defaultMessage + ". Отзыв: " + review);
            }

            log.error("При добавлении/обновлении отзыва {} произошла ошибка, отличная от ошибки заполнения полей",
                    review);
            throw new ReviewValidationException("При добавлении/обновлении отзыва произошла ошибка, " +
                    "отличная от ошибки заполнения полей");
        }
    }
}
