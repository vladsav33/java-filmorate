package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film extends BaseModel {
    //целочисленный идентификатор — id;
    //название — name;
    //описание — description;
    //дата релиза — releaseDate;
    //продолжительность фильма — duration.
    @NotBlank
    private String name;
    @Size(max = 200, message = "Не более 200 знаков")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likes;
}
