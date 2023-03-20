package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
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
    }

    public void removeFriend(int userId, int friendId) {
        checkUserId(userId);
        checkUserId(friendId);

        User user = userStorage.getById(userId).get();

        if (user.getFriends() == null) {
            return;
        }

        user.getFriends().remove(friendId);
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
        userStorage.getById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + id + " не найден."));
    }
}
