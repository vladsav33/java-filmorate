package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null ) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return userStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null ) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return userStorage.deleteFriend(userId, friendId);
    }

    public Set<User> getFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(otherId) == null ) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        return userStorage.getCommonFriends(userId, otherId);
    }
}
