package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import org.springframework.boot.validation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {

    private static int idCounter = 0;
    private int id;
    @NotBlank
    private String login;
    private String name;
    @Email
    private String email;
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

    public boolean validate() {
        if (email == null || email.isEmpty() || !email.contains("@") ||
                login == null || login.isEmpty() || login.contains(" ") ||
                birthday.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }
}
