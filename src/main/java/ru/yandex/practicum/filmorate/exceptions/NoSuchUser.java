package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchUser extends RuntimeException {
    public NoSuchUser(String message) {
        super(message);
    }
}
