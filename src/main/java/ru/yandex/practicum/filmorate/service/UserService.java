package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        checkUserId(userId);
        checkUserId(friendId);

        User user = userStorage.getById(userId).get();

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        user.getFriends().add(friendId);
        log.debug("Добавлен в друзья пользователь ID = {} пользователю: {}", friendId, user);
    }

    public void removeFriend(int userId, int friendId) {
        checkUserId(userId);
        checkUserId(friendId);

        User user = userStorage.getById(userId).get();

        if (user.getFriends() == null) {
            return;
        }

        user.getFriends().remove(friendId);
        log.debug("Удален из друзей пользователь ID = {} у пользователя: {}", friendId, user);
    }

    public Collection<User> getCommonFriends(int userId, int otherUserId) {
        checkUserId(userId);
        checkUserId(otherUserId);

        Set<Integer> commonFriendIds = userStorage.getById(userId).get().getFriends();
        Set<Integer> otherUserFriendIds = userStorage.getById(otherUserId).get().getFriends();

        commonFriendIds.retainAll(otherUserFriendIds);

        return commonFriendIds.stream()
                .map(id -> userStorage.getById(id).get())
                .collect(Collectors.toList());
    }

    private void checkUserId(int id) {
        log.warn("Пользователь с ID = " + id + " не найден.");
        userStorage.getById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + id + " не найден."));
    }
}
