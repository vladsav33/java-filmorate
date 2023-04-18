package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyHasFriendException extends RuntimeException {
    public UserAlreadyHasFriendException(String message) {
        super(message);
    }
}
