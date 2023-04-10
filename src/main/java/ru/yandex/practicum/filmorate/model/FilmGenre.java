package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class FilmGenre {
    private int filmId;
    private int genreId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", filmId);
        values.put("genre_id", genreId);
        return values;
    }
}
