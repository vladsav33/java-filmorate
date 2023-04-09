package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchGenre extends RuntimeException {
    public NoSuchGenre(String message) {
        super(message);
    }
}
