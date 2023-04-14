package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

@Deprecated
@Component("inMemoryUserStorage")
public class InMemoryUserStorage extends BaseModelStorage<User> implements UserStorage {
}
