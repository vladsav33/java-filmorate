package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchMpa extends RuntimeException {
    public NoSuchMpa(String message) {
        super(message);
    }
}
