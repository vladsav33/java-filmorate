package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class, FilmController.class})
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(NoSuchUser.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NoSuchUser exception) {
        log.warn("User was not found");
        return Map.of("Object not found", "No such user");
    }

    @ExceptionHandler(NoSuchFilm.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NoSuchFilm exception) {
        log.warn("Film was not found");
        return Map.of("Object not found", "No such film");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ValidationException exception) {
        log.warn("Data validation error");
        return Map.of("Data validation error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalErrors(final RuntimeException exception) {
        log.warn("Internal Server Error");
        return Map.of("Internal Server Error", exception.getMessage());
    }
}
