package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User extends BaseModel {
    //целочисленный идентификатор — id;
    //электронная почта — email;
    //логин пользователя — login;
    //имя для отображения — name;
    //дата рождения — birthday.
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends;
}

