package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FilmSearchCriteria {
    private final int count;
    private final int genreId;
    private final int year;
    private final boolean byRating;
}
