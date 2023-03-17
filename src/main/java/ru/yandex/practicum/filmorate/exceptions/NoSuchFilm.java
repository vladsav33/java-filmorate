package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchFilm extends RuntimeException {
    public NoSuchFilm(String message) {
        super(message);
    }
}
