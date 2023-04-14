package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    public Collection<User> get();

    public Optional<User> getById(int id);

    public User create(User user);

    public Optional<User> update(User user);
}
