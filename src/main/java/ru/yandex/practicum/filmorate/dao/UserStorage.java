package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> get();

    Optional<User> getById(int id);

    User create(User user);

    Optional<User> update(User user);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    void removeUser(int userId);
}
