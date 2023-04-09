package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data

@Builder
public class Film {
    private static int idCounter = 0;
    private int id;



    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Map<String, Object> mpa;
    private List<Map<String, Object>> genres;
    private List<Integer> likes;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Map<String, Object> mpa, List<Map<String, Object>> genres, List<Integer> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = new LinkedList<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("releaseDate", releaseDate);
        values.put("duration", duration);
        if (mpa != null) {
            values.put("rating_id", mpa.get("id"));
        }
        return values;
    }
}