package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

@Data
public class Film {
    private static int idCounter = 0;
    private int id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public boolean validate() {
        if (name == null ||  name.isEmpty() || description.length() > 200 ||
                releaseDate.isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) ||
                Duration.ofMinutes(duration).isNegative() || Duration.ofMinutes(duration).isZero()) {
            return false;
        }
        return true;
    }
}

