package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private static int idCounter = 0;
    private int id;
    @NotEmpty
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @Email
    @NotEmpty
    private String email;
    @PastOrPresent
    private LocalDate birthday;

    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        if (name == null) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.email = email;
        this.birthday = birthday;
    }
}
