package ru.yandex.practicum.filmorate.exception;

public class RatingValidationException extends RuntimeException {
    public RatingValidationException(String message) {
        super(message);
    }
}
