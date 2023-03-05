package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int idCounter = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.warn("The list of users returned");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (!user.validate()) {
            log.warn("Validation test failed");
            throw new ValidationException();
        }
        user.setId(++idCounter);
        users.put(user.getId(), user);
        log.info("Another user {} is added", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException, NoSuchUser {
        if (!user.validate()) {
            log.warn("Validation test failed");
            throw new ValidationException();
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        users.put(user.getId(), user);
        log.info("User {} is updated", user);
        return user;
    }
}
