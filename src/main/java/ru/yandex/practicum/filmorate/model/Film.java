package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film extends BaseModel {
    //целочисленный идентификатор — id;
    //название — name;
    //описание — description;
    //дата релиза — releaseDate;
    //продолжительность фильма — duration;
    //режиссеры - directors.
    @NotBlank
    private String name;
    @Size(max = 200, message = "Не более 200 знаков")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonIgnore
    private Map<Integer, Integer> likes;
    private Set<Genre> genres;
    private MPA mpa;
    private Set<Director> directors;

    public double getAverageRating() {
        if (likes != null) {
            return likes.values().stream().filter(value -> value != 0).mapToDouble(Integer::doubleValue).average().orElse(0);
        } else {
            return 0;
        }
    }

}
