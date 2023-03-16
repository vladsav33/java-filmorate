package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private final Set<Integer> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        if (name == null || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.email = email;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

}


