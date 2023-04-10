package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class Likes {
    private int userId;
    private int filmId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", userId);
        values.put("film_id", filmId);
        return values;
    }
}
