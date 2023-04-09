package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userStorage")
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User addFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return userStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(otherId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser("Such user was not found");
        }
        return userStorage.getCommonFriends(userId, otherId);
    }
}
