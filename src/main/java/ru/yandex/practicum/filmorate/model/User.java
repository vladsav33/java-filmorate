package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
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
    private List<Integer> friends;

    public User(int id, String login, String name, String email, LocalDate birthday, List<Integer> friends) {
        this.id = id;
        this.login = login;
        if (name == null || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.email = email;
        this.birthday = birthday;
        this.friends = friends;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("name", name);
        values.put("email", email);
        values.put("birthday", birthday);
        return values;
    }

}


