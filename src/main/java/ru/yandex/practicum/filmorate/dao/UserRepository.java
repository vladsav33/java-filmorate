package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter;

    public Collection<User> getUsers() {
        return users.values();
    }

    public User createUser(User user) {
        user.setId(++idCounter);
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }
}
